package org.inflearngg.oauth.client.login.discord;

import lombok.Getter;
import org.inflearngg.oauth.client.login.OAuthLoginParams;
import org.inflearngg.oauth.domain.OAuthProvider;
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
