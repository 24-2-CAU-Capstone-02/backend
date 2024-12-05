package com.capstone.backend.controller;

import com.capstone.backend.dto.request.MenuImageUrlRequest;
import com.capstone.backend.dto.request.SessionTokenRequest;
import com.capstone.backend.dto.request.MenuUpdateRequest;
import com.capstone.backend.dto.response.MenuImageUrlResponse;
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

    @Operation(summary = "메뉴 수동 수정", description = "menuId에 해당하는 메뉴를 수동으로 수정합니다.")
    @PatchMapping("/{menuId}")
    public ResponseEntity<MenuResponse> updateMenu(@PathVariable Long menuId, @Valid @RequestBody MenuUpdateRequest request) {
        Menu menu = menuService.getMenuById(menuId);
        Menu updateMenu = menuService.updateMenu(menu, request);

        MenuResponse response = MenuResponse.getMenuResponse(updateMenu);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "메뉴 수동 삭제", description = "menuId에 해당하는 메뉴를 수동으로 삭제합니다.")
    @DeleteMapping("/{menuId}")
    public ResponseEntity<Void> deleteMenu(@PathVariable Long menuId, @Valid @RequestBody SessionTokenRequest request) {
        Menu menu = menuService.getMenuById(menuId);
        menuService.deleteMenu(menu, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "네이버 이미지 API 사용", description = "네이버 이미지 API로 메뉴의 이미지를 불러옵니다.")
    @PostMapping("/image")
    public ResponseEntity<MenuImageUrlResponse> getMenuImageUrl(@Valid @RequestBody MenuImageUrlRequest request) {
        String imageUrl = menuService.getMenuImageUrl(request);
        MenuImageUrlResponse response = MenuImageUrlResponse.builder().imageUrl(imageUrl).build();
        return ResponseEntity.ok(response);
    }
}
