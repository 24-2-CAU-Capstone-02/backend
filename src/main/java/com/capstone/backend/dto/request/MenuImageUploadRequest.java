package com.capstone.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuImageUploadRequest {
    @NotNull
    private String sessionToken;
    @NotNull
    private List<String> imageUrls;
}
