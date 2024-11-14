package com.capstone.backend.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class RoomResponse {
    private Long id;
    private List<MemberResponse> memberList;
}
