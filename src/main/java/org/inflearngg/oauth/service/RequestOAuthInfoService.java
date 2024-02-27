package org.inflearngg.oauth.service;

import org.inflearngg.oauth.client.KakaoApiClient;
import org.inflearngg.oauth.client.OAuthClient;
import org.inflearngg.oauth.client.login.OAuthLoginParams;
import org.inflearngg.oauth.domain.OAuthProvider;
import org.inflearngg.oauth.domain.OAuthUserInfo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RequestOAuthInfoService {
    private final Map<OAuthProvider, OAuthClient> clients;

    public RequestOAuthInfoService(List<OAuthClient> clients) {
        this.clients = clients.stream().collect(Collectors.toUnmodifiableMap(OAuthClient::oAuthProvider, Function.identity()));
    }

    public OAuthUserInfo request(OAuthLoginParams params) {
        OAuthClient client = clients.get(params.oAuthProvider());
        String accessToken = client.requestAccessToken(params);
        return client.requestOAuthInfo(accessToken);
    }
}
