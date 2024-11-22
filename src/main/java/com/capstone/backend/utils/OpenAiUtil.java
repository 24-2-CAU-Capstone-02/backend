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
import lombok.RequiredArgsConstructor;
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
        // 이미지 base64로 압축
        String base64Image = ImageUtil.compressImage(imageUrl);

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
                "The following image is a menu written in Korean. Please extract each menu item and its price and return them as a pair. " +
                "Additionally, include the generalized name, a brief description, allergy information, and spiciness level (spicyLevel) for each menu item.\n\n" +
                "The generalized name represents a simplified version of the menu item’s name. " +
                "For example, 'Homemade Crispy Spring Rolls' could be generalized to 'Spring Rolls'. " +
                "This information helps users easily understand the essence of the menu and compare or group similar items." +
                "- If the price is displayed as \"free,\" please process it as 0.\n" +
                "- If the price is abbreviated, such as '6.0,' be sure to convert it into whole Korean won and return only the numeric value. For example: '6.0' → '6000'.\n" +
                "- The spiciness level (spicyLevel) should be expressed as a number from 0 to 5. (e.g., 'Spicy' → '5', 'Moderate' → '3', 'Not Spicy' → '0')\n\n" +
                "- The description should be between 10 and 30 characters long." +
                "- The description and allergy information must be detailed and easy for foreigners to understand." +
                "The result format should be as follows:\n" +
                "[\n" +
                "  {\n" +
                "    \"menuName\": \"Menu name\",            // The name as shown on the menu\n" +
                "    \"price\": \"Price (numeric only)\",    // Numeric price\n" +
                "    \"generalizedName\": \"Generalized name\", // Simplified generalized name\n" +
                "    \"description\": \"Brief description\", // Short description of the menu item\n" +
                "    \"allergy\": \"Allergy information\",   // Allergy-causing ingredients\n" +
                "    \"spicyLevel\": \"Spiciness level (1-5)\" // Spiciness level (as a number)\n" +
                "  }\n" +
                "]\n\n" +
                "Example:\n" +
                "[\n" +
                "  {\n" +
                "    \"menuName\": \"아메리카노\",\n" +
                "    \"price\": \"0\",\n" +
                "    \"generalizedName\": \"아메리카노\",\n" +
                "    \"description\": \"에스프레소에 물을 추가한 커피\",\n" +
                "    \"allergy\": \"없음\",\n" +
                "    \"spicyLevel\": \"0\"\n" +
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
        imageUrlMap.put("url", "data:image/jpeg;base64," + base64Image);
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