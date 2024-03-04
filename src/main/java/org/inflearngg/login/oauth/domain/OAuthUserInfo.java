package org.inflearngg.login.oauth.domain;

public interface OAuthUserInfo {
    String getEmail();
    OAuthProvider getOAuthProvider();
}
