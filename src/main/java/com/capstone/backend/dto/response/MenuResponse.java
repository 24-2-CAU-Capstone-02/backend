package com.capstone.backend.dto.response;

import lombok.Builder;

@Builder
public class MenuResponse {
    private Long id;
    private Long roomId;
    private Long imageId;
    private String menuName;
    private Integer price;
}
