package com.capstone.backend.controller;

import com.capstone.backend.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Menu API", description = "메뉴 관련 API")
@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuController {
    private final MenuService menuService;

    @Operation(summary = "주문할 메뉴 선택", description = "menuId에 해당하는 메뉴를 선택합니다.")
    @PostMapping("/{menuId}/choice")
    public ResponseEntity<Void> choiceMenu(@PathVariable String menuId) {
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "메뉴 수동 수정", description = "menuId에 해당하는 메뉴를 수동으로 수정합니다.")
    @PatchMapping("/{menuId}")
    public ResponseEntity<Void> updateMenu(@PathVariable String menuId) {
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "메뉴 수동 삭제", description = "menuId에 해당하는 메뉴를 수동으로 삭제합니다.")
    @DeleteMapping("/{menuId}")
    public ResponseEntity<Void> deleteMenu(@PathVariable String menuId) {
        return ResponseEntity.ok().build();
    }
}
