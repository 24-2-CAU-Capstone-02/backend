package com.capstone.backend.dto.response;

import com.capstone.backend.entity.MenuImage;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class MenuImageResponse {
    private Long id;
    private String imageUrl;

    public static MenuImageResponse getMenuImageResponse(MenuImage menuImage) {
        return MenuImageResponse.builder()
                .id(menuImage.getId())
                .imageUrl(menuImage.getImageUrl())
                .build();
    }

    public static List<MenuImageResponse> getMenuImageResponseList(List<MenuImage> menuImages) {
        List<MenuImageResponse> menuImageResponseList = new ArrayList<>();
        for (MenuImage menuImage : menuImages) {
            menuImageResponseList.add(getMenuImageResponse(menuImage));
        }
        return menuImageResponseList;
    }
}
