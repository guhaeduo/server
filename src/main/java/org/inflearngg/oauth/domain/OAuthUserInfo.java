package org.inflearngg.oauth.domain;

public interface OAuthUserInfo {
    String getEmail();
    OAuthProvider getOAuthProvider();
}
