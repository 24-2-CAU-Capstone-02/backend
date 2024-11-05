package com.capstone.backend.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberResponse {
    private Long id;
    private String username;
}
