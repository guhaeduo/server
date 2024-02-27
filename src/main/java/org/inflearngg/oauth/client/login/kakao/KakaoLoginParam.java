package org.inflearngg.oauth.client.login.kakao;

import lombok.Getter;
import org.inflearngg.oauth.client.login.OAuthLoginParams;
import org.inflearngg.oauth.domain.OAuthProvider;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Getter
public class KakaoLoginParam implements OAuthLoginParams {

    private String authorizeCode;

    @Override
    public OAuthProvider oAuthProvider() {
        return OAuthProvider.KAKAO;
    }

    @Override
    public MultiValueMap<String, String> makeBody() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", authorizeCode);
        return body;
    }
}
