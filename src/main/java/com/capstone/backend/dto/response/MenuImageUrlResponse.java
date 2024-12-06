package com.capstone.backend.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MenuImageUrlResponse {
    private String imageUrl;
    private String subUrl;
}
