package com.capstone.backend.controller;

import com.capstone.backend.dto.request.MenuChoiceRequest;
import com.capstone.backend.dto.request.MenuDeleteRequest;
import com.capstone.backend.dto.request.MenuUpdateRequest;
import com.capstone.backend.dto.response.MenuResponse;
import com.capstone.backend.entity.Menu;
import com.capstone.backend.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
    public ResponseEntity<Void> choiceMenu(@PathVariable Long menuId, @Valid @RequestBody MenuChoiceRequest request) {
        Menu menu = menuService.getMenuById(menuId);
        menuService.choiceMenu(menu, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "메뉴 수동 수정", description = "menuId에 해당하는 메뉴를 수동으로 수정합니다.")
    @PatchMapping("/{menuId}")
    public ResponseEntity<MenuResponse> updateMenu(@PathVariable Long menuId, @Valid @RequestBody MenuUpdateRequest request) {
        Menu menu = menuService.getMenuById(menuId);
        Menu updateMenu = menuService.updateMenu(menu, request);
        MenuResponse response = menuService.getMenuResponse(updateMenu);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "메뉴 수동 삭제", description = "menuId에 해당하는 메뉴를 수동으로 삭제합니다.")
    @DeleteMapping("/{menuId}")
    public ResponseEntity<Void> deleteMenu(@PathVariable Long menuId, @Valid @RequestBody MenuDeleteRequest request) {
        Menu menu = menuService.getMenuById(menuId);
        menuService.deleteMenu(menu, request);
        return ResponseEntity.ok().build();
    }
}
