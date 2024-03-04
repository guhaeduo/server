package org.inflearngg.login.oauth.client.login;

import org.inflearngg.login.oauth.domain.OAuthProvider;
import org.springframework.util.MultiValueMap;

public interface OAuthLoginParams {
    OAuthProvider oAuthProvider();
    MultiValueMap<String, String> makeBody();

}
