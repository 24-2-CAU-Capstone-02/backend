package com.capstone.backend.utils;

import com.capstone.backend.dto.response.MenuItemResponse;
import com.capstone.backend.exception.CustomException;
import com.capstone.backend.exception.ErrorCode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class OpenAiUtil {

    private final WebClient webClient;

    public OpenAiUtil(@Value("${api-key.gpt}") String API_KEY) {
        this.webClient = WebClient.builder()
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + API_KEY)
                .build();
    }

    public List<MenuItemResponse> analyzeImages(String imageUrl) {
        // JSON 요청 본문 생성
        String requestBody = buildRequestBody(imageUrl);

        // OPENAI API 호출
        Mono<String> responsemono = webClient.post()
                .uri("https://api.openai.com/v1/chat/completions")
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> {
                    return clientResponse.bodyToMono(String.class)
                            .doOnNext(errorBody -> System.err.println("Error Body: " + errorBody))
                            .flatMap(errorBody -> Mono.error(new RuntimeException("API Error: " + errorBody)));
                })
                .bodyToMono(String.class);

         return processResponse(responsemono);
    }

    private String buildRequestBody(String imageUrl) {
        // 이미지 base64로 압축
//        String base64Image = ImageUtil.compressImage(imageUrl);

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
                        "The generalized name represents a simplified version of the menu item’s name but should still be specific enough to describe the unique identity of the dish, commonly understood by people. " +
                        "For example, 'Homemade Crispy Spring Rolls' could be generalized to 'Crispy Spring Rolls' rather than just 'Spring Rolls'. " +
                        "Similarly, '된장찌개' should be generalized to '된장찌개' (Soybean Paste Stew) and '김치찌개' should be generalized to '김치찌개' (Kimchi Stew) rather than just '찌개'. " +
                        "This ensures the name is specific and recognizable.\n\n" +
                        "- If the price is displayed as 'free,' please process it as 0.\n" +
                        "- If the price is abbreviated, such as '6.0,' be sure to convert it into whole Korean won and return only the numeric value. For example: '6.0' → '6000'.\n" +
                        "- The spiciness level (spicyLevel) should be expressed as a number from 0 to 5. (e.g., 'Spicy' → '5', 'Moderate' → '2', 'Not Spicy' → '0').\n\n" +
                        "- The description must provide a clear and detailed explanation of the food, including its ingredients, preparation method, appearance, and how it is typically consumed, ensuring that someone with no prior knowledge of the food can easily understand what it is.\n" +
                        "- Provide detailed allergy information about the dish, including all potential allergens (e.g., nuts, shellfish, dairy, gluten, soy, eggs) and their sources within the recipe or preparation process. Specify cross-contamination risks during preparation, cooking, or serving, and include hidden ingredients that might contain allergens (e.g., soy in soy sauce, eggs in mayonnaise, gluten in seasonings)." +
                        " Ensure the information is thorough enough to help individuals with severe allergies make informed decisions.\n\n" +
                        "The result format should be as follows:\n" +
                        "[\n" +
                        " {\n" +
                        " \"menuName\": \"Menu name\", // The name as shown on the menu\n" +
                        " \"price\": \"Price (numeric only)\", // Numeric price\n" +
                        " \"generalizedName\": \"Generalized name\", // Simplified generalized name\n" +
                        " \"description\": \"Brief description\", // Short description of the menu item\n" +
                        " \"allergy\": \"Allergy information\", // Allergy-causing ingredients\n" +
                        " \"spicyLevel\": \"Spiciness level (0-5)\" // Spiciness level (as a number)\n" +
                        " }\n" +
                        "]\n\n" +
                        "Example:\n" +
                        "[\n" +
                        " {\n" +
                        " \"menuName\": \"아메리카노\",\n" +
                        " \"price\": \"0\",\n" +
                        " \"generalizedName\": \"아메리카노\",\n" +
                        " \"description\": \"에스프레소에 물을 추가한 커피\",\n" +
                        " \"allergy\": \"없음\",\n" +
                        " \"spicyLevel\": \"0\"\n" +
                        " },\n" +
                        " {\n" +
                        " \"menuName\": \"된장찌개\",\n" +
                        " \"price\": \"8000\",\n" +
                        " \"generalizedName\": \"된장찌개\",\n" +
                        " \"description\": \"된장과 야채를 넣어 끓인 찌개 요리\",\n" +
                        " \"allergy\": \"대두 포함\",\n" +
                        " \"spicyLevel\": \"1\"\n" +
                        " },\n" +
                        " {\n" +
                        " \"menuName\": \"김치찌개\",\n" +
                        " \"price\": \"9000\",\n" +
                        " \"generalizedName\": \"김치찌개\",\n" +
                        " \"description\": \"김치와 돼지고기를 넣어 끓인 매콤한 찌개\",\n" +
                        " \"allergy\": \"돼지고기 포함\",\n" +
                        " \"spicyLevel\": \"3\"\n" +
                        " }\n" +
                        "]"
//                "The following image is a menu written in Korean. Please extract each menu item and its price and return them as a pair. " +
//                "Additionally, include the generalized name, a brief description, allergy information, and spiciness level (spicyLevel) for each menu item.\n\n" +
//                "The generalized name represents a simplified version of the menu item’s name. " +
//                "For example, 'Homemade Crispy Spring Rolls' could be generalized to 'Spring Rolls'. " +
//                "This information helps users easily understand the essence of the menu and compare or group similar items." +
//                "- If the price is displayed as \"free,\" please process it as 0.\n" +
//                "- If the price is abbreviated, such as '6.0,' be sure to convert it into whole Korean won and return only the numeric value. For example: '6.0' → '6000'.\n" +
//                "- The spiciness level (spicyLevel) should be expressed as a number from 0 to 5. (e.g., 'Spicy' → '5', 'Moderate' → '3', 'Not Spicy' → '0')\n\n" +
//                "- The description should be between 10 and 30 characters long." +
//                "- The description and allergy information must be detailed and easy for foreigners to understand." +
//                "The result format should be as follows:\n" +
//                "[\n" +
//                "  {\n" +
//                "    \"menuName\": \"Menu name\",            // The name as shown on the menu\n" +
//                "    \"price\": \"Price (numeric only)\",    // Numeric price\n" +
//                "    \"generalizedName\": \"Generalized name\", // Simplified generalized name\n" +
//                "    \"description\": \"Brief description\", // Short description of the menu item\n" +
//                "    \"allergy\": \"Allergy information\",   // Allergy-causing ingredients\n" +
//                "    \"spicyLevel\": \"Spiciness level (1-5)\" // Spiciness level (as a number)\n" +
//                "  }\n" +
//                "]\n\n" +
//                "Example:\n" +
//                "[\n" +
//                "  {\n" +
//                "    \"menuName\": \"아메리카노\",\n" +
//                "    \"price\": \"0\",\n" +
//                "    \"generalizedName\": \"아메리카노\",\n" +
//                "    \"description\": \"에스프레소에 물을 추가한 커피\",\n" +
//                "    \"allergy\": \"없음\",\n" +
//                "    \"spicyLevel\": \"0\"\n" +
//                "  },\n" +
//                "  {\n" +
//                "    \"menuName\": \"매운 해물찜\",\n" +
//                "    \"price\": \"12000\",\n" +
//                "    \"generalizedName\": \"해물찜\",\n" +
//                "    \"description\": \"고춧가루를 사용한 매운 양념과 조개, 오징어, 새우 등의 해물을 곁들인 찜 요리\",\n" +
//                "    \"allergy\": \"조개류 포함\",\n" +
//                "    \"spicyLevel\": \"5\"\n" +
//                "  }\n" +
//                "]"
        );


        contentList.add(textContent);

        // 이미지 URL 객체 추가
        Map<String, Object> imageContent = new HashMap<>();
        imageContent.put("type", "image_url");
        Map<String, String> imageUrlMap = new HashMap<>();
//        imageUrlMap.put("url", "data:image/jpeg;base64," + base64Image);
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

    private List<MenuItemResponse> processResponse(Mono<String> responseMono) throws CustomException {
        List<MenuItemResponse> menuItems = new ArrayList<>();

        try {
            String response = responseMono.block();
            JsonObject root = JsonParser.parseString(response).getAsJsonObject();
            JsonArray choices = root.getAsJsonArray("choices");

            if (choices != null && choices.size() > 0) {
                JsonObject message = choices.get(0).getAsJsonObject().getAsJsonObject("message");
                String content = message.get("content").getAsString();
                String cleanedContent = content.replace("```json", "").replace("```", "").trim();

                // `MenuItemResponse` 리스트로 변환
                Gson gson = new Gson();
                Type listType = new TypeToken<List<MenuItemResponse>>() {}.getType();
                menuItems.addAll(gson.fromJson(cleanedContent, listType));
            }
        } catch (JsonSyntaxException e) {
            throw new CustomException(ErrorCode.JSON_MAPPING_ERROR);
        }

        return menuItems;
    }
}