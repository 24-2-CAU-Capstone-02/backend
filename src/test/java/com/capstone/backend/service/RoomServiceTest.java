package com.capstone.backend.service;

import com.capstone.backend.dto.request.MenuImageUploadRequest;
import com.capstone.backend.dto.request.RoomAddMemberRequest;
import com.capstone.backend.dto.response.RoomResponse;
import com.capstone.backend.dto.response.TokenResponse;
import com.capstone.backend.entity.Member;
import com.capstone.backend.entity.Menu;
import com.capstone.backend.entity.Room;
import com.capstone.backend.exception.CustomException;
import com.capstone.backend.repository.MemberRepository;
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
class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private RoomService roomService;

    private Room room;
    private Member member;

    @BeforeEach
    void setUp() {
        member = Member.builder().id(1L).build();
        room = Room.builder()
                .id(1L)
                .member(member)
                .build();
    }

    @Test
    void getRoomById_Success() {
        // given
        Long roomId = 1L;
        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));

        // when
        Room result = roomService.getRoomById(roomId);

        // then
        assertNotNull(result);
        assertEquals(room.getId(), result.getId());
        verify(roomRepository, times(1)).findById(roomId);
    }

    @Test
    void getRoomById_Failure() {
        // given
        Long roomId = 2L;
        when(roomRepository.findById(roomId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(CustomException.class, () -> roomService.getRoomById(roomId));
        verify(roomRepository, times(1)).findById(roomId);
    }

    @Test
    void getMenuListInRoom_Success() {
        // given
        List<Menu> menuList = List.of(Menu.builder().id(1L).build());
        when(menuRepository.findAllByRoom(room)).thenReturn(menuList);

        // when
        List<Menu> result = roomService.getMenuListInRoom(room);

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(menuList.get(0).getId(), result.get(0).getId());
        verify(menuRepository, times(1)).findAllByRoom(room);
    }

    @Test
    void getMenuListInRoom_Failure() {
        // given
        Room nonExistentRoom = Room.builder().id(99L).build();
        when(menuRepository.findAllByRoom(nonExistentRoom)).thenReturn(List.of());

        // when
        List<Menu> result = roomService.getMenuListInRoom(nonExistentRoom);

        // then
        assertTrue(result.isEmpty());
        verify(menuRepository, times(1)).findAllByRoom(nonExistentRoom);
    }

    @Test
    void createRoom_Success() {
        // given
        RoomCreateRequest request = new RoomCreateRequest("validToken", "username", "password");
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        // when
        Room createdRoom = roomService.createRoom(request);

        // then
        assertNotNull(createdRoom);
        assertEquals(room.getId(), createdRoom.getId());
        verify(roomRepository, times(1)).save(any(Room.class));
    }

    @Test
    void uploadMenuBoardImage_Success() {
        // given
        MenuImageUploadRequest request = new MenuImageUploadRequest("validToken", List.of("http://example.com/image.jpg"));
        when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));

        // when
        roomService.uploadMenuBoardImage(room, request);

        // then
        verify(roomRepository, times(1)).save(room);
    }

    @Test
    void uploadMenuBoardImage_Failure() {
        // given
        MenuImageUploadRequest request = new MenuImageUploadRequest("validToken", List.of("http://example.com/image.jpg"));
        Room nonExistentRoom = Room.builder().id(99L).build();

        when(roomRepository.findById(nonExistentRoom.getId())).thenReturn(Optional.empty());

        // when & then
        assertThrows(CustomException.class, () -> roomService.uploadMenuBoardImage(nonExistentRoom, request));
        verify(roomRepository, never()).save(any());
    }

    @Test
    void addMemberInRoom_Success() {
        // given
        RoomAddMemberRequest request = new RoomAddMemberRequest("username", "password");
        when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));
        when(memberRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(member));

        // when
        TokenResponse tokenResponse = roomService.addMemberInRoom(room, request);

        // then
        assertNotNull(tokenResponse);
        assertEquals(member.getId(), room.getMember().getId());
        verify(roomRepository, times(1)).save(room);
    }

    @Test
    void addMemberInRoom_Failure() {
        // given
        RoomAddMemberRequest request = new RoomAddMemberRequest("username", "password");
        Room nonExistentRoom = Room.builder().id(99L).build();

        when(roomRepository.findById(nonExistentRoom.getId())).thenReturn(Optional.empty());

        // when & then
        assertThrows(CustomException.class, () -> roomService.addMemberInRoom(nonExistentRoom, request));
        verify(roomRepository, never()).save(any());
    }

    @Test
    void getRoomResponse_Success() {
        // when
        RoomResponse response = roomService.getRoomResponse(room);

        // then
        assertNotNull(response);
        assertEquals(room.getId(), response.getId());
    }
}