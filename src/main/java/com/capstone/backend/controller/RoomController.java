package com.capstone.backend.controller;

import com.capstone.backend.service.MemberService;
import com.capstone.backend.service.MenuImageService;
import com.capstone.backend.service.MenuService;
import com.capstone.backend.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Room API", description = "방 관련 API")
@RestController
@RequestMapping("/room")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;
    private final MenuImageService menuImageService;
    private final MemberService memberService;
    private final MenuService menuService;

    @Operation(summary = "방 정보 확인", description = "roomId에 해당하는 방의 정보를 조회합니다.")
    @GetMapping("/{roomId}")
    public ResponseEntity<Void> getRoom(@PathVariable String roomId) {
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "메뉴 목록 확인", description = "roomId에 해당하는 방의 메뉴 목록을 조회합니다.")
    @GetMapping("/{roomId}/menu")
    public ResponseEntity<Void> getMenuList(@PathVariable String roomId) {
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "방 생성", description = "방을 생성합니다.")
    @PostMapping
    public ResponseEntity<Void> createRoom() {
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "메뉴판 이미지 등록", description = "roomId에 해당하는 방에 메뉴판 이미지를 업로드하여 등록합니다.")
    @PostMapping("/{roomId}/upload")
    public ResponseEntity<Void> uploadMenuBoardImage(@PathVariable String roomId) {
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "방 멤버 추가", description = "roomId에 해당하는 방에 멤버를 추가합니다.")
    @PostMapping("/{roomId}/member")
    public ResponseEntity<Void> addMember(@PathVariable String roomId) {
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "방의 메뉴판 정보 공유", description = "roomId에 해당하는 방에 등록된 메뉴판 정보를 실제 가게 정보와 연결하여 외부에 공개합니다.")
    @PostMapping("/{roomId}/share")
    public ResponseEntity<Void> shareRoomMenu(@PathVariable String roomId) {
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "메뉴 수동 추가", description = "roomId에 해당하는 방에 메뉴를 수동으로 추가합니다.")
    @PostMapping("/{roomId}/menu")
    public ResponseEntity<Void> addMenu(@PathVariable String roomId) {
        return ResponseEntity.ok().build();
    }
}
