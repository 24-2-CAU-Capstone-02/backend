package com.capstone.backend.service;

import com.capstone.backend.dto.request.MenuCreateRequest;
import com.capstone.backend.dto.request.MenuImageUrlRequest;
import com.capstone.backend.dto.request.MenuUpdateRequest;
import com.capstone.backend.dto.request.SessionTokenRequest;
import com.capstone.backend.dto.response.MenuImageUrlResponse;
import com.capstone.backend.entity.Member;
import com.capstone.backend.entity.Menu;
import com.capstone.backend.entity.Room;
import com.capstone.backend.exception.CustomException;
import com.capstone.backend.exception.ErrorCode;
import com.capstone.backend.repository.MenuRepository;
import com.capstone.backend.utils.NaverUtil;
import com.capstone.backend.utils.SessionUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    private final SessionUtil sessionUtil;
    private final NaverUtil naverUtil;

    public Menu getMenuById(Long menuId) throws CustomException {
        return menuRepository.findById(menuId)
                .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));
    }

    public List<Menu> getMenuListInRoom(Room room) throws CustomException {
        return menuRepository.findAllByRoom(room);
    }

    public Menu updateMenu(Menu menu, MenuUpdateRequest request) throws CustomException {
        Member member = sessionUtil.getMemberBySessionToken(request.getSessionToken());
        if (!isValidMemberWithMenu(menu, member)) {
            throw new CustomException(ErrorCode.MEMBER_NOT_AUTHORIZED);
        }

        if (request.getMenuName() != null) {
            menu.setMenuName(request.getMenuName());
        }
        if (request.getPrice() != null) {
            menu.setPrice(request.getPrice());
        }

        menuRepository.save(menu);
        return menu;
    }

    public void deleteMenu(Menu menu, SessionTokenRequest request) throws CustomException {
        Member member = sessionUtil.getMemberBySessionToken(request.getSessionToken());
        if (!isValidMemberWithMenu(menu, member)) {
            throw new CustomException(ErrorCode.MEMBER_NOT_AUTHORIZED);
        }

        menu.setStatus("deleted");
        menuRepository.save(menu);
    }

    public Menu createMenu(Room room, MenuCreateRequest request) throws CustomException {
        Member member = sessionUtil.getMemberBySessionToken(request.getSessionToken());
        if (!Objects.equals(member.getRoom().getId(), room.getId())) {
            throw new CustomException(ErrorCode.MEMBER_NOT_AUTHORIZED);
        }

        Menu menu = Menu.builder()
                .room(room)
                .image(null)
                .menuName(request.getMenuName())
                .price(request.getPrice())
                .status("added")
                .build();

        menuRepository.save(menu);
        return menu;
    }

    public MenuImageUrlResponse getMenuImageUrl(MenuImageUrlRequest request) throws CustomException {
        return naverUtil.getImageUrl(request.getMenuName());
    }

    private boolean isValidMemberWithMenu(Menu menu, Member member) {
        Room room = menu.getRoom();
        return Objects.equals(member.getRoom().getId(), room.getId());
    }
}
