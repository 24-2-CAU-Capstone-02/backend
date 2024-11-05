package com.capstone.backend.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MenuResponse {
    private Long id;
    private Long roomId;
    private Long imageId;
    private String menuName;
    private Integer price;
}
