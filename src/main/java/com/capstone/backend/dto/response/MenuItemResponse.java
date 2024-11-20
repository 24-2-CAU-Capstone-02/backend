package com.capstone.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemResponse {
    private String menuName;
    private String price;
    private String generalizedName;
    private String description;
    private String allergy;
    private int spicyLevel;
}
