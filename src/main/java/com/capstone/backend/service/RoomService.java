package com.capstone.backend.service;

import com.capstone.backend.dto.request.MenuImageUploadRequest;
import com.capstone.backend.dto.request.RoomAddMemberRequest;
import com.capstone.backend.dto.response.MemberResponse;
import com.capstone.backend.dto.response.MenuItemResponse;
import com.capstone.backend.dto.response.RoomResponse;
import com.capstone.backend.entity.*;
import com.capstone.backend.exception.CustomException;
import com.capstone.backend.exception.ErrorCode;
import com.capstone.backend.repository.*;
import com.capstone.backend.utils.OpenAiUtil;
import com.capstone.backend.utils.S3Util;
import com.capstone.backend.utils.SessionUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class RoomService {
    private final MenuRepository menuRepository;
    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;
    private final MenuImageRepository menuImageRepository;
    private final OpenAiUtil openAiUtil;
    private final S3Util s3Util;
    private final SessionUtil sessionUtil;

    public Room getRoomById(Long roomId) throws CustomException {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.ROOM_NOT_FOUND));
    }

    public Room createRoom() throws CustomException {
        Room room = Room.builder().build();
        roomRepository.save(room);

        return room;
    }

    public void uploadMenuBoardImage(Room room, MenuImageUploadRequest request) throws CustomException {
        List<String> imageUrls = request.getImageUrls();
        for (String imageUrl : imageUrls) {
            MenuImage menuImage = MenuImage.builder()
                    .room(room)
                    .imageUrl(imageUrl)
                    .status("normal")
                    .build();

            menuImageRepository.save(menuImage);

            List<MenuItemResponse> menuItemResponses = openAiUtil.analyzeImages(imageUrl);
            for (MenuItemResponse menuItemResponse : menuItemResponses) {
                Menu menu = Menu.builder()
                        .room(room)
                        .image(menuImage)
                        .menuName(menuItemResponse.getMenuName())
                        .price(Integer.parseInt(menuItemResponse.getPrice()))
                        .status(menuItemResponse.getDescription())
                        .build();

                menuRepository.save(menu);
            }
        }
    }

    public void uploadMenuBoardImageFormData(Room room, List<MultipartFile> images) throws CustomException {
        for (MultipartFile image : images) {
            String imageUrl = s3Util.uploadImage(image);

            MenuImage menuImage = MenuImage.builder()
                    .room(room)
                    .imageUrl(imageUrl)
                    .status("normal")
                    .build();

            menuImageRepository.save(menuImage);

            List<MenuItemResponse> menuItemResponses = openAiUtil.analyzeImages(imageUrl);
            for (MenuItemResponse menuItemResponse : menuItemResponses) {
                Menu menu = Menu.builder()
                        .room(room)
                        .image(menuImage)
                        .menuName(menuItemResponse.getMenuName())
                        .price(Integer.parseInt(menuItemResponse.getPrice()))
                        .description(menuItemResponse.getDescription())
                        .status("added")
                        .generalizedName(menuItemResponse.getGeneralizedName())
                        .allergy(menuItemResponse.getAllergy())
                        .spicyLevel(menuItemResponse.getSpicyLevel())
                        .build();

                menuRepository.save(menu);
            }
        }
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
