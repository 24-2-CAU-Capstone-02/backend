package com.capstone.backend.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoomCreateResponse {
    private Long roomId;
    private String sessionToken;
}
