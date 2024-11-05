package com.capstone.backend.service;

import com.capstone.backend.dto.request.MenuImageDeleteRequest;
import com.capstone.backend.entity.MenuImage;
import com.capstone.backend.exception.CustomException;
import com.capstone.backend.repository.MenuImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuImageServiceTest {

    @Mock
    private MenuImageRepository menuImageRepository;

    @InjectMocks
    private MenuImageService menuImageService;

    private MenuImage menuImage;

    @BeforeEach
    void setUp() {
        menuImage = MenuImage.builder()
                .id(1L)
                .imageUrl("http://example.com/image.jpg")
                .build();
    }

    @Test
    void getMenuImageById_Success() {
        // given
        Long imageId = 1L;
        when(menuImageRepository.findById(imageId)).thenReturn(Optional.of(menuImage));

        // when
        MenuImage result = menuImageService.getMenuImageById(imageId);

        // then
        assertNotNull(result);
        assertEquals(menuImage.getImageUrl(), result.getImageUrl());
        verify(menuImageRepository, times(1)).findById(imageId);
    }

    @Test
    void getMenuImageById_Failure() {
        // given
        Long imageId = 2L;
        when(menuImageRepository.findById(imageId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(CustomException.class, () -> menuImageService.getMenuImageById(imageId));
        verify(menuImageRepository, times(1)).findById(imageId);
    }

    @Test
    void deleteMenuImage_Success() {
        // given
        MenuImageDeleteRequest request = new MenuImageDeleteRequest("validToken");
        when(menuImageRepository.findById(menuImage.getId())).thenReturn(Optional.of(menuImage));

        // when
        menuImageService.deleteMenuImage(menuImage, request);

        // then
        verify(menuImageRepository, times(1)).delete(menuImage);
    }

    @Test
    void deleteMenuImage_Failure() {
        // given
        MenuImageDeleteRequest request = new MenuImageDeleteRequest("validToken");
        MenuImage nonExistentImage = MenuImage.builder().id(99L).build();

        when(menuImageRepository.findById(nonExistentImage.getId())).thenReturn(Optional.empty());

        // when & then
        assertThrows(CustomException.class, () -> menuImageService.deleteMenuImage(nonExistentImage, request));

        verify(menuImageRepository, times(1)).findById(nonExistentImage.getId());
        verify(menuImageRepository, never()).delete(any());
    }
}