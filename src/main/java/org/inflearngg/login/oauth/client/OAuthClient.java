package org.inflearngg.login.oauth.client;

import org.inflearngg.login.oauth.accesstoken.OAuthAccessCode;
import org.inflearngg.login.oauth.client.login.OAuthLoginParams;
import org.inflearngg.login.oauth.domain.OAuthProvider;
import org.inflearngg.login.oauth.domain.OAuthUserInfo;

public interface OAuthClient {
    OAuthProvider oAuthProvider();

    String requestAccessCode(OAuthAccessCode codes);

    String requestAccessToken(OAuthLoginParams params);

    OAuthUserInfo requestOAuthInfo(String accessToken);

}
