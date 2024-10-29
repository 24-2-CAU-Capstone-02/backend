package com.capstone.backend.controller;

import com.capstone.backend.dto.request.MemberUpdateRequest;
import com.capstone.backend.service.MenuImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Image API", description = "메뉴판 이미지 관련 API")
@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageController {
    private final MenuImageService menuImageService;

    @Operation(summary = "메뉴판 이미지 삭제", description = "imageId에 해당하는 메뉴판 이미지를 삭제합니다. 메뉴판 이미지로 생성된 메뉴들도 전부 삭제됩니다.")
    @DeleteMapping("/{imageId}")
    public ResponseEntity<Void> deleteMenuImage(@PathVariable String imageId, @Valid @RequestBody MemberUpdateRequest request) {
        menuImageService.deleteMenuImage(imageId, request);
        return ResponseEntity.ok().build();
    }
}
