package org.inflearngg.login.oauth.client.login.discord;

import lombok.Getter;
import org.inflearngg.login.oauth.domain.OAuthProvider;
import org.inflearngg.login.oauth.client.login.OAuthLoginParams;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Getter
public class DiscordLoginParam implements OAuthLoginParams {

    private String authorizeCode;
    @Override
    public OAuthProvider oAuthProvider() {
        return OAuthProvider.DISCORD;
    }

    @Override
    public MultiValueMap<String, String> makeBody() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", authorizeCode);
        body.add("scope", "email");
        return body;
    }
}
