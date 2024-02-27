package org.inflearngg.oauth.client;

import org.inflearngg.oauth.client.login.OAuthLoginParams;
import org.inflearngg.oauth.domain.OAuthProvider;
import org.inflearngg.oauth.domain.OAuthUserInfo;

public interface OAuthClient {
    OAuthProvider oAuthProvider();

    String requestAccessToken(OAuthLoginParams params);

    OAuthUserInfo requestOAuthInfo(String accessToken);

}
