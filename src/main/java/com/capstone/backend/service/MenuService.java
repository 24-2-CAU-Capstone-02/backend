package com.capstone.backend.service;

import com.capstone.backend.dto.request.MenuChoiceRequest;
import com.capstone.backend.dto.request.MenuCreateRequest;
import com.capstone.backend.dto.request.MenuUpdateRequest;
import com.capstone.backend.dto.request.SessionTokenRequest;
import com.capstone.backend.entity.Member;
import com.capstone.backend.entity.Menu;
import com.capstone.backend.entity.Room;
import com.capstone.backend.exception.CustomException;
import com.capstone.backend.exception.ErrorCode;
import com.capstone.backend.repository.MenuRepository;
import com.capstone.backend.repository.RoomRepository;
import com.capstone.backend.utils.SessionUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    private final RoomRepository roomRepository;
    private final SessionUtil sessionUtil;

    public Menu getMenuById(Long menuId) throws CustomException {
        return menuRepository.findById(menuId)
                .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));
    }

    public void choiceMenu(Menu menu, MenuChoiceRequest request) throws CustomException {
        Member member = sessionUtil.getMemberBySessionToken(request.getSessionToken());
        if (!isValidMemberWithMenu(menu, member)) {
            throw new CustomException(ErrorCode.MEMBER_NOT_AUTHORIZED);
        }

        // TODO : 웹소켓
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
                .ocrInfo(null)
                .menuName(request.getMenuName())
                .price(request.getPrice())
                .status("added")
                .build();

        menuRepository.save(menu);
        return menu;
    }

    private boolean isValidMemberWithMenu(Menu menu, Member member) {
        Room room = menu.getRoom();
        return Objects.equals(member.getRoom().getId(), room.getId());
    }
}
