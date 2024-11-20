package com.capstone.backend.utils;

import com.capstone.backend.dto.response.MenuItemResponse;
import com.capstone.backend.exception.CustomException;
import com.capstone.backend.exception.ErrorCode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class OpenAiUtil {

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

    @Value("${api-key.gpt}")
    private String API_KEY;


    public List<MenuItemResponse> analyzeImages(String imageUrl) {
        // Gson 객체 생성
        Gson gson = new Gson();

        // RestTemplate 초기화
        RestTemplate restTemplate = new RestTemplate();

        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + API_KEY);

        // JSON 요청 본문 생성
        String requestBody = buildRequestBody(imageUrl);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        try {
            // OpenAI API 호출
            ResponseEntity<byte[]> responseEntity = restTemplate.exchange(
                    OPENAI_API_URL,
                    HttpMethod.POST,
                    entity,
                    byte[].class
            );

            //응답 데이터를 UTF-8로 인코딩
            String responsebody = new String(responseEntity.getBody(), StandardCharsets.UTF_8);
            List<MenuItemResponse> menuItemResponses = parseResponse(responsebody);
            return menuItemResponses;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("OpenAI API 호출 실패", e);
        }
    }

    private String buildRequestBody(String imageUrl) {
        // 요청 본문 생성
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-4o");

        // Messages 배열 생성
        Map<String, Object> message = new HashMap<>();
        message.put("role", "user");

        // content 배열 생성
        List<Map<String, Object>> contentList = new ArrayList<>();

        // 텍스트 객체 추가
        Map<String, Object> textContent = new HashMap<>();
        textContent.put("type", "text");
        textContent.put("text",
                "다음 이미지는 한국어로 작성된 메뉴판입니다. 메뉴판에서 각 메뉴와 가격을 짝지어 반환해 주세요. " +
                "추가로, 각 메뉴의 일반화된 이름, 간단한 소개, 알레르기 유발 성분 정보, 맵기 수준(spicyLevel)을 함께 반환해 주세요.\n\n" +
                "일반화된 이름(generalizedName)은 메뉴의 특정한 이름을 보다 간단하게 표현한 이름입니다. " +
                "예를 들어, '홈메이드 크리스피 춘권'이라는 메뉴는 '춘권'으로 일반화될 수 있습니다. " +
                "이 정보는 사용자가 메뉴의 본질을 쉽게 이해하고 유사한 메뉴를 그룹화하거나 비교할 수 있도록 도와줍니다." +
                "- 가격이 \"무료\"로 표시된 경우에는 0으로 처리해 주세요.\n" +
                "- 가격이 '6.0'과 같이 축약된 경우에는 반드시 원 단위로 변환하여 숫자만 반환해 주세요. 예: '6.0' → '6000'.\n" +
                "- 맵기 수준(spicyLevel)은 1~5까지 숫자로 표현해 주세요. (예: '매움' → '5', '중간' → '3', '안 매움' → '1')\n\n" +
                "- 설명(description)은 10-30자 정도로 작성해 주세요." +
                "결과 형식은 다음과 같습니다:\n" +
                "[\n" +
                "  {\n" +
                "    \"menuName\": \"메뉴 이름\",           // 메뉴판에 표시된 이름\n" +
                "    \"price\": \"가격 (숫자만)\",           // 숫자 형태의 가격\n" +
                "    \"generalizedName\": \"일반화된 이름\", // 간단히 일반화된 이름\n" +
                "    \"description\": \"간단한 소개\",       // 메뉴 설명\n" +
                "    \"allergy\": \"알레르기 정보\",     // 알레르기 유발 성분\n" +
                "    \"spicyLevel\": \"맵기 수준 (1-5 숫자)\" // 맵기 정도 (숫자로 반환)\n" +
                "  }\n" +
                "]\n\n" +
                "예시:\n" +
                "[\n" +
                "  {\n" +
                "    \"menuName\": \"아메리카노\",\n" +
                "    \"price\": \"0\",\n" +
                "    \"generalizedName\": \"아메리카노\",\n" +
                "    \"description\": \"에스프레소에 물을 추가한 커피\",\n" +
                "    \"allergy\": \"없음\",\n" +
                "    \"spicyLevel\": \"1\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"menuName\": \"매운 해물찜\",\n" +
                "    \"price\": \"12000\",\n" +
                "    \"generalizedName\": \"해물찜\",\n" +
                "    \"description\": \"고춧가루를 사용한 매운 양념과 조개, 오징어, 새우 등의 해물을 곁들인 찜 요리\",\n" +
                "    \"allergy\": \"조개류 포함\",\n" +
                "    \"spicyLevel\": \"5\"\n" +
                "  }\n" +
                "]");

        contentList.add(textContent);

        // 이미지 URL 객체 추가
        Map<String, Object> imageContent = new HashMap<>();
        imageContent.put("type", "image_url");
        Map<String, String> imageUrlMap = new HashMap<>();
        imageUrlMap.put("url", imageUrl);
        imageContent.put("image_url", imageUrlMap);
        contentList.add(imageContent);

        // 메시지 content에 배열 추가
        message.put("content", contentList);

        // Messages 배열에 메시지 추가
        List<Map<String, Object>> messages = new ArrayList<>();
        messages.add(message);

        requestBody.put("messages", messages);
        requestBody.put("max_tokens", 1500);

        // Gson을 사용해 JSON 직렬화
        Gson gson = new Gson();
        return gson.toJson(requestBody);
    }

    private List<MenuItemResponse> parseResponse(String responseBody) {
        List<MenuItemResponse> menuItems = new ArrayList<>();
        try {
            Gson gson = new Gson();

            JsonObject root = JsonParser.parseString(responseBody).getAsJsonObject();
            JsonArray choices = root.getAsJsonArray("choices");

            if (choices.size() > 0) {
                JsonObject choice = choices.get(0).getAsJsonObject();
                JsonObject message = choice.getAsJsonObject("message");
                String content = message.get("content").getAsString();
                System.out.println(content);

                // content 내부의 JSON 배열 추출
                String replaced = content.replace("```json", "").replace("```", "");
                Type listType = new TypeToken<List<MenuItemResponse>>() {}.getType();
                menuItems = gson.fromJson(replaced, listType);
            }
        } catch (Exception e) {
            throw new CustomException(ErrorCode.JSON_MAPPING_ERROR);
        }

        return menuItems;
    }
}