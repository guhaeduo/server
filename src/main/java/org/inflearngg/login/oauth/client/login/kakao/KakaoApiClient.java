package org.inflearngg.login.oauth.client.login.kakao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.inflearngg.login.oauth.accesstoken.OAuthAccessCode;
import org.inflearngg.login.oauth.client.OAuthClient;
import org.inflearngg.login.oauth.client.login.OAuthLoginParams;
import org.inflearngg.login.oauth.domain.OAuthProvider;
import org.inflearngg.login.oauth.domain.OAuthUserInfo;
import org.inflearngg.login.oauth.domain.kakao.KakaoUserInfo;
import org.inflearngg.login.oauth.client.OAuthConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoApiClient implements OAuthClient {


    @Value("${oauth.kakao.url.base}")
    private String baseUrl;
    @Value("${oauth.kakao.url.token}")
    private String tokenUrl;
    @Value("${oauth.kakao.url.profile}")
    private String profileUrl;

    @Value("${oauth.kakao.client-id}")
    private String clientId;
    @Value("${oauth.kakao.client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate;

    @Override
    public OAuthProvider oAuthProvider() {
        return OAuthProvider.KAKAO;
    }

    @Override
    public String requestAccessCode(OAuthAccessCode code) {
        String authorizeUrl = code.makeQueryParams(baseUrl, clientId);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(authorizeUrl, String.class);
        log.info("responseEntity : {}", responseEntity);
        String uri = responseEntity.getHeaders().getLocation().toString();
        log.info("uri : {}", uri);
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(uri).build();
        return uriComponents.getQueryParams().getFirst("code");
    }

    @Override
    public String requestAccessToken(OAuthLoginParams params) {
        HttpEntity<MultiValueMap<String, String>> request = generateHttpRequest(params);
        KakaoToken kakaoToken = restTemplate.postForObject(tokenUrl, request, KakaoToken.class);
        Objects.requireNonNull(kakaoToken, "카카오 토큰을 가져오는데 실패했습니다.");
        return kakaoToken.accessToken();
    }


    @Override
    public OAuthUserInfo requestOAuthInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        //추가적인 정보가 있을 경우
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("property_keys", "[\"kakao_account.email\"]");

        HttpEntity<?> request = new HttpEntity<>(body, headers);

        return restTemplate.postForObject(profileUrl, request, KakaoUserInfo.class);
    }


    private HttpEntity<MultiValueMap<String, String>> generateHttpRequest(OAuthLoginParams params) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> body = params.makeBody();
        body.add("grant_type", OAuthConstant.GRANT_TYPE);
        body.add("client_id", clientId);
        body.add("redirect_uri", "http://localhost:8080/api/oauth/kakao/callback");

        return new HttpEntity<>(body, headers);
    }

}
