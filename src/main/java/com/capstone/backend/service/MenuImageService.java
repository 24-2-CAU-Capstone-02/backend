package com.capstone.backend.service;

import com.capstone.backend.dto.request.SessionTokenRequest;
import com.capstone.backend.entity.Member;
import com.capstone.backend.entity.Menu;
import com.capstone.backend.entity.MenuImage;
import com.capstone.backend.exception.CustomException;
import com.capstone.backend.exception.ErrorCode;
import com.capstone.backend.repository.MenuImageRepository;
import com.capstone.backend.repository.MenuRepository;
import com.capstone.backend.utils.SessionUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class MenuImageService {
    private final MenuImageRepository menuImageRepository;
    private final MenuRepository menuRepository;
    private final SessionUtil sessionUtil;

    public MenuImage getMenuImageById(Long imageId) throws CustomException {
        return menuImageRepository.findById(imageId)
                .orElseThrow(() -> new CustomException(ErrorCode.MENU_IMAGE_NOT_FOUND));
    }

    public void deleteMenuImage(MenuImage menuImage, SessionTokenRequest request) {
        Member member = sessionUtil.getMemberBySessionToken(request.getSessionToken());
        if (!Objects.equals(member.getRoom().getId(), menuImage.getRoom().getId())) {
            throw new CustomException(ErrorCode.MEMBER_NOT_AUTHORIZED);
        }

        menuImage.setStatus("deleted");
        menuImageRepository.save(menuImage);

        List<Menu> menuList = menuRepository.findAllByImage(menuImage);
        for (Menu menu : menuList) {
            menu.setStatus("deleted");
            menuRepository.save(menu);
        }
    }
}
