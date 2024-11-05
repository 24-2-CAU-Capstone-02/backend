package com.capstone.backend.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenResponse {
    private String username;
    private String sessionToken;
}
