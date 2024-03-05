package org.inflearngg.login.oauth.client.login.kakao;

import lombok.Getter;
import org.inflearngg.login.oauth.domain.OAuthProvider;
import org.inflearngg.login.oauth.client.login.OAuthLoginParams;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Getter
public class KakaoLoginParam implements OAuthLoginParams {

    private String authorizeCode;
    private String redirectUri;

    @Override
    public OAuthProvider oAuthProvider() {
        return OAuthProvider.KAKAO;
    }

    @Override
    public MultiValueMap<String, String> makeBody() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", authorizeCode);
        body.add("redirect_uri", redirectUri);
        return body;
    }
}
