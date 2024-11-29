package com.capstone.backend.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class RoomCreateResponse {
    private UUID roomId;
}
