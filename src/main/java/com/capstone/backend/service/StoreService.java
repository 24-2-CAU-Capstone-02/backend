package com.capstone.backend.service;

import com.capstone.backend.dto.response.StoreResponse;
import com.capstone.backend.entity.Store;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class StoreService {
    public Store getStoreById(Long storeId) {
        return null;
    }

    public StoreResponse getStoreResponse(Store store) {
        return null;
    }
}
