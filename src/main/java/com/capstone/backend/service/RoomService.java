package com.capstone.backend.service;

import com.capstone.backend.dto.request.MenuImageUploadRequest;
import com.capstone.backend.dto.request.RoomAddMemberRequest;
import com.capstone.backend.dto.response.MemberResponse;
import com.capstone.backend.dto.response.RoomResponse;
import com.capstone.backend.entity.*;
import com.capstone.backend.exception.CustomException;
import com.capstone.backend.exception.ErrorCode;
import com.capstone.backend.repository.*;
import com.capstone.backend.utils.AsyncUtil;
import com.capstone.backend.utils.S3Util;
import com.capstone.backend.utils.SessionUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
@Transactional
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;
    private final MenuImageRepository menuImageRepository;
    private final AccountRepository accountRepository;
    private final S3Util s3Util;
    private final SessionUtil sessionUtil;
    private final AsyncUtil asyncUtil;

    private final int DAILY_ROOM_LIMIT = 2;
    private final int ROOM_MENU_IMAGE_LIMIT = 5;

    public Room getRoomById(Long roomId) throws CustomException {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.ROOM_NOT_FOUND));
    }

    public Room createRoom(UserDetails userDetails) throws CustomException {
        Account account = accountRepository.findByGoogleId(userDetails.getUsername())
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        LocalDate today = LocalDate.now();
        if (today.equals(account.getLastRoomCreatedDate())) {
            if (account.getDailyRoomCount() >= DAILY_ROOM_LIMIT) {
                throw new CustomException(ErrorCode.EXCEEDED_DAILY_ROOM_LIMIT);
            }
        } else {
            account.setLastRoomCreatedDate(today);
            account.resetDailyRoomCount();
        }

        Room room = Room.builder()
                .account(account)
                .build();
        account.incrementDailyRoomCount();

        accountRepository.save(account);
        roomRepository.save(room);

        return room;
    }

    public void uploadMenuBoardImage(Room room, MenuImageUploadRequest request) throws CustomException {
        List<MenuImage> menuImages = menuImageRepository.findAllByRoom(room);
        List<String> imageUrls = request.getImageUrls();
        if (menuImages.size() + imageUrls.size() > ROOM_MENU_IMAGE_LIMIT) {
            throw new CustomException(ErrorCode.MENU_IMAGE_TOO_MANY);
        }

        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (String imageUrl : imageUrls) {
            futures.add(asyncUtil.processImage(imageUrl, room));
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

    public void uploadMenuBoardImageFormData(Room room, List<MultipartFile> images) throws CustomException {
        List<MenuImage> menuImages = menuImageRepository.findAllByRoom(room);
        if (menuImages.size() + images.size() > ROOM_MENU_IMAGE_LIMIT) {
            throw new CustomException(ErrorCode.MENU_IMAGE_TOO_MANY);
        }

        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (MultipartFile image : images) {
            String imageUrl = s3Util.uploadImage(image);
            futures.add(asyncUtil.processImage(imageUrl, room));
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

    public Member addMemberToRoom(Room room, RoomAddMemberRequest request) throws CustomException {
        Member existedMember = memberRepository.findByRoomAndUsername(room, request.getUsername()).orElse(null);
        if (existedMember != null) {
            if (!Objects.equals(existedMember.getPassword(), request.getPassword())) {
                throw new CustomException(ErrorCode.PASSWORD_NOT_CORRECT);
            }
            String sessionToken = sessionUtil.generateUUIDToken();
            existedMember.setSessionToken(sessionToken);
            memberRepository.save(existedMember);
            return existedMember;
        }

        String sessionToken = sessionUtil.generateUUIDToken();
        Member member = Member.builder()
                .room(room)
                .username(request.getUsername())
                .password(request.getPassword())
                .sessionToken(sessionToken)
                .build();
        memberRepository.save(member);

        return member;
    }

    public RoomResponse getRoomResponse(Room room) throws CustomException {
        List<MemberResponse> memberList = memberRepository.findAllByRoom(room).stream()
                .map(MemberResponse::getMemberResponse)
                .toList();

        return RoomResponse.builder()
                .id(room.getId())
                .memberList(memberList)
                .build();
    }
}
