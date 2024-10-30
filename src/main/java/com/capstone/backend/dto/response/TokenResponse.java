package com.capstone.backend.dto.response;

import lombok.Builder;

@Builder
public class TokenResponse {
    private String username;
    private String sessionToken;
}
