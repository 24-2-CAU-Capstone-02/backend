package com.capstone.backend.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class RoomResponse {
    private UUID id;
    private List<MemberResponse> memberList;
}
