package com.capstone.backend.dto.response;

import com.capstone.backend.entity.MenuOcrInfo;
import com.capstone.backend.exception.CustomException;
import com.capstone.backend.exception.ErrorCode;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OcrResponse {
    private Integer positionX;
    private Integer positionY;
    private Integer width;
    private Integer height;

    public static OcrResponse getOCRResponse(MenuOcrInfo menuOcrInfo) throws CustomException {
        if (menuOcrInfo == null) {
            return null;
        }

        try {
            return OcrResponse.builder()
                    .positionX(menuOcrInfo.getPositionX())
                    .positionY(menuOcrInfo.getPositionY())
                    .width(menuOcrInfo.getWidth())
                    .height(menuOcrInfo.getHeight())
                    .build();
        } catch (Exception e) {
            throw new CustomException(ErrorCode.MAPPING_ERROR);
        }
    }
}
