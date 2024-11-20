package com.capstone.backend.controller;

import com.capstone.backend.dto.request.*;
import com.capstone.backend.dto.response.*;
import com.capstone.backend.entity.Member;
import com.capstone.backend.entity.Menu;
import com.capstone.backend.entity.MenuImage;
import com.capstone.backend.entity.Room;
import com.capstone.backend.service.MenuImageService;
import com.capstone.backend.service.MenuService;
import com.capstone.backend.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Room API", description = "방 관련 API")
@RestController
@RequestMapping("/room")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;
    private final MenuService menuService;
    private final MenuImageService menuImageService;

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
        List<Menu> menuList = menuService.getMenuListInRoom(room);
        List<MenuResponse> response = MenuResponse.getMenuResponseList(menuList);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "업로드된 메뉴판 이미지 목록 확인", description = "roomId에 해당하는 방에 업로드된 메뉴판 이미지 목록을 조회합니다.")
    @GetMapping("/{roomId}/image")
    public ResponseEntity<List<MenuImageResponse>> getMenuImageList(@PathVariable Long roomId) {
        Room room = roomService.getRoomById(roomId);
        List<MenuImage> menuImageList = menuImageService.getMenuImageListInRoom(room);
        List<MenuImageResponse> response = MenuImageResponse.getMenuImageResponseList(menuImageList);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "방 생성", description = "방을 생성합니다.")
    @PostMapping
    public ResponseEntity<RoomCreateResponse> createRoom() {
        Room room = roomService.createRoom();
        RoomCreateResponse response = RoomCreateResponse.builder()
                .roomId(room.getId())
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "메뉴판 이미지 등록", description = "roomId에 해당하는 방에 메뉴판 이미지를 업로드하여 등록합니다. 이미지를 url로 업로드")
    @PostMapping("/{roomId}/upload")
    public ResponseEntity<Void> uploadMenuBoardImage(@PathVariable Long roomId, @Valid @RequestBody MenuImageUploadRequest request) {
        Room room = roomService.getRoomById(roomId);
        roomService.uploadMenuBoardImage(room, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "form data를 사용한 메뉴판 이미지 등록", description = "roomId에 해당하는 방에 메뉴판 이미지를 업로드하여 등록합니다. 이미지를 폼데이터로 업로드")
    @PostMapping("/{roomId}/upload/image")
    public ResponseEntity<Void> uploadMenuBoardImageInFormData(@PathVariable Long roomId, @RequestParam("images") List<MultipartFile> images) {
        Room room = roomService.getRoomById(roomId);
        roomService.uploadMenuBoardImageFormData(room, images);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "방 로그인 또는 새 멤버 추가", description = "roomId에 해당하는 방에 로그인을 시도합니다. 요청에 담긴 멤버 이름에 해당하는 멤버가 방에 존재하지 않는다면 해당 요청을 바탕으로 새 멤버를 추가합니다. 멤버 이름을 가진 멤버는 존재하지만 비밀번호가 틀리다면 요청을 실패합니다.")
    @PostMapping("/{roomId}/member")
    public ResponseEntity<TokenResponse> addMember(@PathVariable Long roomId, @Valid @RequestBody RoomAddMemberRequest request) {
        Room room = roomService.getRoomById(roomId);
        Member member = roomService.addMemberToRoom(room, request);
        TokenResponse response = TokenResponse.builder()
                .memberId(member.getId())
                .username(member.getUsername())
                .sessionToken(member.getSessionToken())
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "메뉴 수동 추가", description = "roomId에 해당하는 방에 메뉴를 수동으로 추가합니다.")
    @PostMapping("/{roomId}/menu")
    public ResponseEntity<MenuResponse> addMenu(@PathVariable Long roomId, @Valid @RequestBody MenuCreateRequest request) {
        Room room = roomService.getRoomById(roomId);
        Menu menu = menuService.createMenu(room, request);

        MenuResponse response = MenuResponse.getMenuResponse(menu);
        return ResponseEntity.ok(response);
    }
}
