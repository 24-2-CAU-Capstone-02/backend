package com.capstone.backend.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedHashMap;

@Component
public class GoogleOAuthUtil {
    @Value("${google.oauth.client_id}")
    private String clientId;
    @Value("${google.oauth.client_secret}")
    private String clientSecret;
    @Value("${google.oauth.redirect_url}")
    private String redirectUrl;

    private final String googleOauthUrl = "https://oauth2.googleapis.com/token";
    private final String googleDataUrl = "https://www.googleapis.com/oauth2/v2/userinfo";

    public String fetchGoogleAccessToken(String code) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUrl);
        params.add("client_secret", clientSecret);
        params.add("code", code);

        HttpEntity<?> http = new HttpEntity<>(params, headers);
        URI uri = new URI(googleOauthUrl);

        ResponseEntity<LinkedHashMap> response = restTemplate.exchange(uri, HttpMethod.POST, http, LinkedHashMap.class);
        return "Bearer " + response.getBody().get("access_token");
    }

    public LinkedHashMap<String, Object> fetchGoogleUserData(String googleAccessToken) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", googleAccessToken);
        HttpEntity<?> http = new HttpEntity<>(headers);
        URI uri = new URI(googleDataUrl);

        ResponseEntity<LinkedHashMap> response = restTemplate.exchange(uri, HttpMethod.GET, http, LinkedHashMap.class);
        return (LinkedHashMap<String, Object>) response.getBody();
    }
}
