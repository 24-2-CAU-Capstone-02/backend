package com.capstone.backend.service;

import com.capstone.backend.dto.request.MenuImageUploadRequest;
import com.capstone.backend.dto.request.RoomAddMemberRequest;
import com.capstone.backend.dto.request.RoomCreateRequest;
import com.capstone.backend.dto.response.RoomResponse;
import com.capstone.backend.dto.response.TokenResponse;
import com.capstone.backend.entity.Menu;
import com.capstone.backend.entity.Room;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RoomService {
    public Room getRoomById(Long roomId) {
        return null;
    }

    public List<Menu> getMenuListInRoom(Room room) {
        return null;
    }

    public Room createRoom(RoomCreateRequest request) {
        return null;
    }

    public void uploadMenuBoardImage(Room room, MenuImageUploadRequest request) {
    }

    public TokenResponse addMemberInRoom(Room room, RoomAddMemberRequest request) {
        return null;
    }

//    public void shareRoomMenu(Room room, ShareRoomMenuRequest request) {
//    }

    public RoomResponse getRoomResponse(Room room) {
        return null;
    }
}
