package com.capstone.backend.controller;

import com.capstone.backend.dto.request.*;
import com.capstone.backend.dto.response.MenuResponse;
import com.capstone.backend.dto.response.RoomResponse;
import com.capstone.backend.dto.response.TokenResponse;
import com.capstone.backend.entity.Menu;
import com.capstone.backend.entity.Room;
import com.capstone.backend.service.MenuService;
import com.capstone.backend.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Room API", description = "방 관련 API")
@RestController
@RequestMapping("/room")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;
    private final MenuService menuService;

    @Operation(summary = "방 정보 확인", description = "roomId에 해당하는 방의 정보를 조회합니다.")
    @GetMapping("/{roomId}")
    public ResponseEntity<RoomResponse> getRoom(@PathVariable Long roomId) {
        Room room = roomService.getRoomById(roomId);
        RoomResponse response = roomService.getRoomResponse(room);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "메뉴 목록 확인", description = "roomId에 해당하는 방의 메뉴 목록을 조회합니다.")
    @GetMapping("/{roomId}/menu")
    public ResponseEntity<List<MenuResponse>> getMenuList(@PathVariable Long roomId) {
        Room room = roomService.getRoomById(roomId);
        List<Menu> menuList = roomService.getMenuListInRoom(room);
        List<MenuResponse> response = menuService.getMenuResponseList(menuList);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "방 생성", description = "방을 생성합니다.")
    @PostMapping
    public ResponseEntity<RoomResponse> createRoom(@Valid @RequestBody RoomCreateRequest request) {
        Room room = roomService.createRoom(request);
        RoomResponse response = roomService.getRoomResponse(room);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "메뉴판 이미지 등록", description = "roomId에 해당하는 방에 메뉴판 이미지를 업로드하여 등록합니다. 업로드된 이미지를 머신러닝 모델을 통해 메뉴 리스트로 변환합니다.")
    @PostMapping("/{roomId}/upload")
    public ResponseEntity<Void> uploadMenuBoardImage(@PathVariable Long roomId, @Valid @RequestBody MenuImageUploadRequest request) {
        Room room = roomService.getRoomById(roomId);
        roomService.uploadMenuBoardImage(room, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "방 멤버 추가", description = "roomId에 해당하는 방에 멤버를 추가합니다.")
    @PostMapping("/{roomId}/member")
    public ResponseEntity<TokenResponse> addMember(@PathVariable Long roomId, @Valid @RequestBody RoomAddMemberRequest request) {
        Room room = roomService.getRoomById(roomId);
        TokenResponse response = roomService.addMemberInRoom(room, request);
        return ResponseEntity.ok(response);
    }

//    @Operation(summary = "방의 메뉴판 정보 공유", description = "roomId에 해당하는 방에 등록된 메뉴판 정보를 실제 가게 정보와 연결하여 외부에 공개합니다.")
//    @PostMapping("/{roomId}/share")
//    public ResponseEntity<Void> shareRoomMenu(@PathVariable Long roomId, @Valid @RequestBody ShareRoomMenuRequest request) {
//        Room room = roomService.getRoomById(roomId);
//        roomService.shareRoomMenu(room, request);
//        return ResponseEntity.ok().build();
//    }

    @Operation(summary = "메뉴 수동 추가", description = "roomId에 해당하는 방에 메뉴를 수동으로 추가합니다.")
    @PostMapping("/{roomId}/menu")
    public ResponseEntity<MenuResponse> addMenu(@PathVariable Long roomId, @Valid @RequestBody MenuCreateRequest request) {
        Room room = roomService.getRoomById(roomId);
        Menu menu = menuService.createMenu(room, request);
        MenuResponse response = menuService.getMenuResponse(menu);
        return ResponseEntity.ok(response);
    }
}
