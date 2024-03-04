package org.inflearngg.login.oauth.client.login.discord;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.inflearngg.login.oauth.domain.OAuthProvider;
import org.inflearngg.login.oauth.domain.OAuthUserInfo;
import org.inflearngg.login.oauth.accesstoken.OAuthAccessCode;
import org.inflearngg.login.oauth.client.OAuthClient;
import org.inflearngg.login.oauth.client.OAuthConstant;
import org.inflearngg.login.oauth.client.login.OAuthLoginParams;
import org.inflearngg.login.oauth.domain.discord.DiscordUserInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Objects;


@Slf4j
@Component
@RequiredArgsConstructor
public class DiscordApiClient implements OAuthClient {

    @Value("${oauth.discord.url.base}")
    private String baseUrl;
    @Value("${oauth.discord.client-id}")
    private String clientId;
    @Value("${oauth.discord.client-secret}")
    private String clientSecret;
    private String redirectUri;

    private final RestTemplate restTemplate;

    @Override
    public OAuthProvider oAuthProvider() {
        return OAuthProvider.DISCORD;
    }

    @Override
    public String requestAccessCode(OAuthAccessCode code) {
        String authorizeUrl = code.makeQueryParams(baseUrl, clientId);
        UriComponentsBuilder response = UriComponentsBuilder.fromHttpUrl(authorizeUrl);
        return response.toUriString();
    }

    @Override
    public String requestAccessToken(OAuthLoginParams params) {
        String tokenUrl = baseUrl + "/oauth2/token";
        HttpEntity<MultiValueMap<String, String>> request = generateHttpRequest(params);
        DiscordToken discordToken = restTemplate.postForObject(tokenUrl, request, DiscordToken.class);
        Objects.requireNonNull(discordToken, "디스코드 토큰을 가져오는데 실패했습니다.");
        return discordToken.accessToken();
    }

    @Override
    public OAuthUserInfo requestOAuthInfo(String accessToken)  {
        String profileUrl = baseUrl + "/users/@me";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Object> entity = new HttpEntity<>(headers);
        ResponseEntity<DiscordUserInfo> exchange = restTemplate.exchange(profileUrl, HttpMethod.GET, entity, DiscordUserInfo.class);
        return exchange.getBody();

    }

    private HttpEntity<MultiValueMap<String, String>> generateHttpRequest(OAuthLoginParams params) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> body = params.makeBody();
        body.add("grant_type", OAuthConstant.GRANT_TYPE);
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("redirect_uri", "http://localhost:8080/api/oauth/discord/callback");
        return new HttpEntity<>(body, headers);
    }
}
