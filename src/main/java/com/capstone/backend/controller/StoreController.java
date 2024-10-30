package com.capstone.backend.controller;

import com.capstone.backend.dto.response.StoreResponse;
import com.capstone.backend.entity.Store;
import com.capstone.backend.service.StoreService;
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
    private final StoreService storeService;

    @Operation(summary = "가게 정보 확인", description = "storeId에 해당하는 가게 정보를 조회합니다.")
    @GetMapping("/{storeId}")
    public ResponseEntity<StoreResponse> getStore(@PathVariable String storeId) {
        Store store = storeService.getStoreById(storeId);
        StoreResponse response = storeService.getStoreResponse(store);
        return ResponseEntity.ok(response);
    }
}
