package com.capstone.backend.service;

import com.capstone.backend.dto.request.MenuChoiceRequest;
import com.capstone.backend.dto.request.MenuCreateRequest;
import com.capstone.backend.dto.request.MenuDeleteRequest;
import com.capstone.backend.dto.request.MenuUpdateRequest;
import com.capstone.backend.dto.response.MenuResponse;
import com.capstone.backend.entity.Member;
import com.capstone.backend.entity.MemberMenu;
import com.capstone.backend.entity.Menu;
import com.capstone.backend.entity.Room;
import com.capstone.backend.exception.CustomException;
import com.capstone.backend.repository.MemberMenuRepository;
import com.capstone.backend.repository.MenuRepository;
import com.capstone.backend.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;
    @Mock
    private MemberMenuRepository memberMenuRepository;
    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private MenuService menuService;

    private Menu menu;
    private Room room;
    private Member member;

    @BeforeEach
    void setUp() {
        member = Member.builder().id(1L).build();
        room = Room.builder().id(1L).build();
        menu = Menu.builder()
                .id(1L)
                .menuName("Test Menu")
                .price(100)
                .room(room)
                .member(member)
                .status("available")
                .build();
    }

    @Test
    void getMenuById_Success() {
        // given
        Long menuId = 1L;
        when(menuRepository.findById(menuId)).thenReturn(Optional.of(menu));

        // when
        Menu result = menuService.getMenuById(menuId);

        // then
        assertNotNull(result);
        assertEquals(menu.getMenuName(), result.getMenuName());
        verify(menuRepository, times(1)).findById(menuId);
    }

    @Test
    void getMenuById_Failure() {
        // given
        Long menuId = 2L;
        when(menuRepository.findById(menuId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(CustomException.class, () -> menuService.getMenuById(menuId));
        verify(menuRepository, times(1)).findById(menuId);
    }

    @Test
    void choiceMenu_Success() {
        // given
        MenuChoiceRequest request = new MenuChoiceRequest("validToken", "chosen");
        when(menuRepository.findById(menu.getId())).thenReturn(Optional.of(menu));

        // when
        menuService.choiceMenu(menu, request);

        // then
        verify(memberMenuRepository, times(1)).save(any(MemberMenu.class));
    }

    @Test
    void choiceMenu_Failure() {
        // given
        MenuChoiceRequest request = new MenuChoiceRequest("validToken", "chosen");
        Menu nonExistentMenu = Menu.builder().id(99L).build();

        when(menuRepository.findById(nonExistentMenu.getId())).thenReturn(Optional.empty());

        // when & then
        assertThrows(CustomException.class, () -> menuService.choiceMenu(nonExistentMenu, request));
        verify(memberMenuRepository, never()).save(any());
    }

    @Test
    void updateMenu_Success() {
        // given
        MenuUpdateRequest request = new MenuUpdateRequest("validToken", "Updated Menu", 200);
        when(menuRepository.findById(menu.getId())).thenReturn(Optional.of(menu));

        // when
        Menu updatedMenu = menuService.updateMenu(menu, request);

        // then
        assertEquals("Updated Menu", updatedMenu.getMenuName());
        assertEquals(200, updatedMenu.getPrice());
        verify(menuRepository, times(1)).save(menu);
    }

    @Test
    void updateMenu_Failure() {
        // given
        MenuUpdateRequest request = new MenuUpdateRequest("validToken", "Updated Menu", 200);
        Menu nonExistentMenu = Menu.builder().id(99L).build();

        when(menuRepository.findById(nonExistentMenu.getId())).thenReturn(Optional.empty());

        // when & then
        assertThrows(CustomException.class, () -> menuService.updateMenu(nonExistentMenu, request));
        verify(menuRepository, never()).save(any());
    }

    @Test
    void deleteMenu_Success() {
        // given
        MenuDeleteRequest request = new MenuDeleteRequest("validToken");
        when(menuRepository.findById(menu.getId())).thenReturn(Optional.of(menu));

        // when
        menuService.deleteMenu(menu, request);

        // then
        verify(menuRepository, times(1)).delete(menu);
    }

    @Test
    void deleteMenu_Failure() {
        // given
        MenuDeleteRequest request = new MenuDeleteRequest("validToken");
        Menu nonExistentMenu = Menu.builder().id(99L).build();

        when(menuRepository.findById(nonExistentMenu.getId())).thenReturn(Optional.empty());

        // when & then
        assertThrows(CustomException.class, () -> menuService.deleteMenu(nonExistentMenu, request));
        verify(menuRepository, never()).delete(any());
    }

    @Test
    void createMenu_Success() {
        // given
        MenuCreateRequest request = new MenuCreateRequest("validToken", "New Menu", 150);
        when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));

        // when
        Menu createdMenu = menuService.createMenu(room, request);

        // then
        assertNotNull(createdMenu);
        assertEquals("New Menu", createdMenu.getMenuName());
        assertEquals(150, createdMenu.getPrice());
        verify(menuRepository, times(1)).save(any(Menu.class));
    }

    @Test
    void createMenu_Failure() {
        // given
        MenuCreateRequest request = new MenuCreateRequest("validToken", "New Menu", 150);
        Room nonExistentRoom = Room.builder().id(99L).build();

        when(roomRepository.findById(nonExistentRoom.getId())).thenReturn(Optional.empty());

        // when & then
        assertThrows(CustomException.class, () -> menuService.createMenu(nonExistentRoom, request));
        verify(menuRepository, never()).save(any());
    }

    @Test
    void getMenuResponse_Success() {
        // when
        MenuResponse response = menuService.getMenuResponse(menu);

        // then
        assertNotNull(response);
        assertEquals(menu.getMenuName(), response.getMenuName());
    }

    @Test
    void getMenuResponseList_Success() {
        // given
        List<Menu> menuList = List.of(menu);

        // when
        List<MenuResponse> responseList = menuService.getMenuResponseList(menuList);

        // then
        assertNotNull(responseList);
        assertEquals(1, responseList.size());
        assertEquals(menu.getMenuName(), responseList.get(0).getMenuName());
    }
}