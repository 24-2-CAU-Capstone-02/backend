package com.capstone.backend.utils;

import com.capstone.backend.dto.response.MenuItemResponse;
import com.capstone.backend.entity.Menu;
import com.capstone.backend.entity.MenuImage;
import com.capstone.backend.entity.Room;
import com.capstone.backend.repository.MenuImageRepository;
import com.capstone.backend.repository.MenuRepository;
import com.capstone.backend.utils.OpenAiUtil;
import com.capstone.backend.utils.S3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class AsyncUtil {

    private final S3Util s3Util;
    private final OpenAiUtil openAiUtil;

    private final MenuRepository menuRepository;
    private final MenuImageRepository menuImageRepository;

    @Async
    public CompletableFuture<Void> processImage(String imageUrl, Room room) {
        MenuImage menuImage = MenuImage.builder()
                .room(room)
                .imageUrl(imageUrl)
                .status("normal")
                .build();

        menuImageRepository.save(menuImage);

        List<MenuItemResponse> menuItemResponses = openAiUtil.analyzeImages(imageUrl);
        for (MenuItemResponse menuItemResponse : menuItemResponses) {
            Menu menu = Menu.builder()
                    .room(room)
                    .image(menuImage)
                    .menuName(menuItemResponse.getMenuName())
                    .price(Integer.parseInt(menuItemResponse.getPrice()))
                    .description(menuItemResponse.getDescription())
                    .status("added")
                    .generalizedName(menuItemResponse.getGeneralizedName())
                    .allergy(menuItemResponse.getAllergy())
                    .spicyLevel(menuItemResponse.getSpicyLevel())
                    .build();

            menuRepository.save(menu);
        }

        return CompletableFuture.completedFuture(null);
    }

    @Async
    public String convertImage(MultipartFile image) {
        return s3Util.uploadImage(image);
    }
}
