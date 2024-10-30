package com.capstone.backend.service;

import com.capstone.backend.dto.request.MemberUpdateRequest;
import com.capstone.backend.dto.request.MenuImageDeleteRequest;
import com.capstone.backend.entity.MenuImage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class MenuImageService {
    public MenuImage getMenuImageById(String imageId) {
        return null;
    }

    public void deleteMenuImage(MenuImage menuImage, MenuImageDeleteRequest request) {
    }
}
