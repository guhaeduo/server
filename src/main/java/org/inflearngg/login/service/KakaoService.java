package org.inflearngg.login.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
public class KakaoService {

    @Value("${kakao.client-id}")
    private String kakaoClientId;

    @Value("${kakao.client-secret}")
    private String kakaoClientSecret;

    private final RestTemplate restTemplate = new RestTemplate();


    public String getLoginUrl() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://kauth.kakao.com/oauth/authorize")
                .queryParam("client_id", kakaoClientId)
                .queryParam("redirect_uri", "http://localhost:8080/api/login/kakao/callback")
                .queryParam("response_type", "code");

        return builder.toUriString();
    }

    public String getAccessToken(String authorizeCode) {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("grant_type", "authorization_code");
        parameters.add("client_id", kakaoClientId);
        parameters.add("redirect_uri", "http://localhost:8080/api/login/kakao/callback");
        parameters.add("code", authorizeCode);
        //보안 설정이 on이면 client_secret을 같이 보내야함
//        parameters.add("client_secret", kakaoClientSecret);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(parameters, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("https://kauth.kakao.com/oauth/token", request, String.class);
        log.info("AccessTokenResponse: " + response.getBody());
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode;
        String accessToken = "";
        try {
            rootNode = objectMapper.readTree(response.getBody());
            accessToken = rootNode.get("access_token").asText();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


        return accessToken;
    }

    public String getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        //추가적인 정보가 있을 경우
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("property_keys", "[\"kakao_account.email\"]");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(parameters, headers);
        //401 에러
        ResponseEntity<String> response = restTemplate.postForEntity("https://kapi.kakao.com/v2/user/me", request, String.class);

        return response.getBody();

    }
}