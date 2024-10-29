package com.capstone.backend.service;

import com.capstone.backend.dto.request.MemberUpdateRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class MenuImageService {
    public void deleteMenuImage(String imageId, MemberUpdateRequest request) {

    }
}
