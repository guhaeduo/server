package org.inflearngg.login.oauth.service;

import lombok.extern.slf4j.Slf4j;
import org.inflearngg.login.oauth.client.login.OAuthLoginParams;
import org.inflearngg.login.oauth.domain.OAuthProvider;
import org.inflearngg.login.oauth.domain.OAuthUserInfo;
import org.inflearngg.login.oauth.accesstoken.OAuthAccessCode;
import org.inflearngg.login.oauth.client.OAuthClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
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

    public String requestAccessCode(OAuthAccessCode params) {
        OAuthClient client = clients.get(params.oAuthProvider());
        return client.requestAccessCode(params);
    }
}
