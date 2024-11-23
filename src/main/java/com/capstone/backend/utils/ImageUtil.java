package com.capstone.backend.utils;

import com.capstone.backend.exception.CustomException;
import com.capstone.backend.exception.ErrorCode;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.Base64;

@Component
public class ImageUtil {
    public static String compressImage(String imageUrl) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Thumbnails.of(new URL(imageUrl))
                    .size(800, 600)
                    .outputQuality(0.5)
                    .toOutputStream(baos);

            return Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.IMAGE_COMPRESS_FAILED);
        }
    }
}
