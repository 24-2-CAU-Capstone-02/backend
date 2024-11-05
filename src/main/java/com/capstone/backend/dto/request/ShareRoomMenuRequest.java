package com.capstone.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShareRoomMenuRequest {
    @NotNull
    private String sessionToken;
    @NotNull
    private String kakaoId;
    @NotNull
    private String storeName;
}
