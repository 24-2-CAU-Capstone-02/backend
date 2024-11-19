package com.capstone.backend.dto.response;

import com.capstone.backend.entity.Menu;
import com.capstone.backend.exception.CustomException;
import com.capstone.backend.exception.ErrorCode;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class MenuResponse {
    private Long id;
    private Long roomId;
    private Long imageId;
    private String menuName;
    private Integer price;
    private String status;

    public static MenuResponse getMenuResponse(Menu menu) throws CustomException {
        try {
            return MenuResponse.builder()
                    .id(menu.getId())
                    .roomId(menu.getRoom().getId())
                    .imageId(menu.getImage() != null ? menu.getImage().getId() : null)
                    .menuName(menu.getMenuName())
                    .price(menu.getPrice())
                    .status(menu.getStatus())
                    .build();
        } catch (Exception e) {
            throw new CustomException(ErrorCode.MAPPING_ERROR);
        }
    }

    public static List<MenuResponse> getMenuResponseList(List<Menu> menuList) {
        List<MenuResponse> menuResponseList = new ArrayList<>();
        for (Menu menu : menuList) {
            menuResponseList.add(getMenuResponse(menu));
        }
        return menuResponseList;
    }
}
