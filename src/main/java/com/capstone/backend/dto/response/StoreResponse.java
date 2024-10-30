package com.capstone.backend.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public class StoreResponse {
    private Long id;
    private List<RoomResponse> roomList;
    private String storeName;
    private String storeLocation;
}
