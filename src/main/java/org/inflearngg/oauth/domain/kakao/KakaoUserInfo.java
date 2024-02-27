package org.inflearngg.oauth.domain.kakao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.inflearngg.oauth.domain.OAuthProvider;
import org.inflearngg.oauth.domain.OAuthUserInfo;

public class KakaoUserInfo implements OAuthUserInfo {

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @JsonIgnoreProperties(ignoreUnknown = true)
    record KakaoAccount(String email) {
    }

    @Override
    public String getEmail() {
        return kakaoAccount.email();
    }

    @Override
    public OAuthProvider getOAuthProvider() {
        return OAuthProvider.KAKAO;
    }
}
