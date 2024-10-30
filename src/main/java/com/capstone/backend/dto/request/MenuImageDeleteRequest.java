package com.capstone.backend.dto.request;

import jakarta.validation.constraints.NotNull;

public class MenuImageDeleteRequest {
    @NotNull
    private String sessionToken;
}
