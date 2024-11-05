package com.capstone.backend.service;

import com.capstone.backend.dto.request.MenuChoiceRequest;
import com.capstone.backend.dto.request.MenuCreateRequest;
import com.capstone.backend.dto.request.MenuDeleteRequest;
import com.capstone.backend.dto.request.MenuUpdateRequest;
import com.capstone.backend.dto.response.MenuResponse;
import com.capstone.backend.entity.Menu;
import com.capstone.backend.entity.Room;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MenuService {
    public Menu getMenuById(Long menuId) {
        return null;
    }

    public void choiceMenu(Menu menu, MenuChoiceRequest request) {
    }

    public Menu updateMenu(Menu menu, MenuUpdateRequest request) {
        return null;
    }

    public void deleteMenu(Menu menu, MenuDeleteRequest request) {
    }

    public Menu createMenu(Room room, MenuCreateRequest request) {
        return null;
    }

    public MenuResponse getMenuResponse(Menu updateMenu) {
        return null;
    }

    public List<MenuResponse> getMenuResponseList(List<Menu> menuList) {
        return null;
    }
}
