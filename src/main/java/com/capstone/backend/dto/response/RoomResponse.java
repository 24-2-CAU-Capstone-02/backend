package com.capstone.backend.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public class RoomResponse {
    private Long id;
    private Long ownerId;
    private Long storeId;
    private Integer totalPrice;
    private List<MemberResponse> memberList;
    private List<MenuResponse> menuList;
}
