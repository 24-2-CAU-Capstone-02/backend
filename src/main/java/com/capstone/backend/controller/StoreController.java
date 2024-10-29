package com.capstone.backend.controller;

import com.capstone.backend.repository.StoreRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Store API", description = "가게 관련 API")
@RestController
@RequestMapping("/store")
@RequiredArgsConstructor
public class StoreController {
    private final StoreRepository storeRepository;

    @Operation(summary = "가게 정보 확인", description = "storeId에 해당하는 가게 정보를 조회합니다.")
    @GetMapping("/{storeId}")
    public ResponseEntity<Void> getStore(@PathVariable String storeId) {
        return ResponseEntity.ok().build();
    }
}
