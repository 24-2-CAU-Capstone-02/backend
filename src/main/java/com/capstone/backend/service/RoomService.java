package com.capstone.backend.service;

import com.capstone.backend.dto.request.MenuImageUploadRequest;
import com.capstone.backend.dto.request.RoomAddMemberRequest;
import com.capstone.backend.dto.response.RoomResponse;
import com.capstone.backend.entity.Member;
import com.capstone.backend.entity.Menu;
import com.capstone.backend.entity.MenuImage;
import com.capstone.backend.entity.Room;
import com.capstone.backend.exception.CustomException;
import com.capstone.backend.exception.ErrorCode;
import com.capstone.backend.repository.MemberRepository;
import com.capstone.backend.repository.MenuImageRepository;
import com.capstone.backend.repository.MenuRepository;
import com.capstone.backend.repository.RoomRepository;
import com.capstone.backend.utils.SessionUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RoomService {
    private final MenuRepository menuRepository;
    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;
    private final MenuImageRepository menuImageRepository;
    private final SessionUtil sessionUtil;

    public Room getRoomById(Long roomId) throws CustomException {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.ROOM_NOT_FOUND));
    }

    public List<Menu> getMenuListInRoom(Room room) {
        return menuRepository.findAllByRoom(room);
    }

    public Room createRoom() throws CustomException {
        Room room = Room.builder().build();
        roomRepository.save(room);

        return room;
    }

    public void uploadMenuBoardImage(Room room, MenuImageUploadRequest request) {
        List<String> imageUrls = request.getImageUrls();
        for (String imageUrl : imageUrls) {
            MenuImage menuImage = MenuImage.builder()
                    .room(room)
                    .imageUrl(imageUrl)
                    .status("normal")
                    .build();

            menuImageRepository.save(menuImage);

            // TODO : 머신러닝 모델과 연결하여 Menu 생성 및 MenuImage와 연결
        }
    }

    public Member addMemberToRoom(Room room, RoomAddMemberRequest request) {
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

    public RoomResponse getRoomResponse(Room room) {
        return null;
    }
}
