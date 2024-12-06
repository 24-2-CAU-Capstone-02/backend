package com.capstone.backend.utils;

import com.capstone.backend.dto.response.MenuImageUrlResponse;
import com.capstone.backend.exception.CustomException;
import com.capstone.backend.exception.ErrorCode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class NaverUtil {
    @Value("${naver.api.client_id}")
    private String clientId;
    @Value("${naver.api.client_secret}")
    private String clientSecret;

    private final String imageSearchUrl = "https://openapi.naver.com/v1/search/image";

    public MenuImageUrlResponse getImageUrl(String keyword) throws CustomException {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(imageSearchUrl)
                .queryParam("query", keyword)
                .queryParam("display", 1)
                .queryParam("start", 1)
                .queryParam("sort", "sim");

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);

        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    uriBuilder.build().toUriString(),
                    HttpMethod.GET,
                    new org.springframework.http.HttpEntity<>(headers),
                    String.class
            );

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            JsonNode itemsNode = rootNode.path("items");
            if (itemsNode.isArray() && !itemsNode.isEmpty()) {
                String imageUrl = itemsNode.get(0).path("link").asText();
                String subUrl = itemsNode.get(0).path("thumbnail").asText();
                return MenuImageUrlResponse.builder()
                        .imageUrl(imageUrl)
                        .subUrl(subUrl)
                        .build();
            } else {
                System.out.println("naver api response parsing error");
                throw new CustomException(ErrorCode.NAVER_API_ERROR);
            }
        } catch (Exception e) {
            System.out.println(keyword + ", " + e.getMessage());
            throw new CustomException(ErrorCode.NAVER_API_ERROR);
        }
    }
}
