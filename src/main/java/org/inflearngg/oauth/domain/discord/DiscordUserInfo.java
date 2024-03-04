package org.inflearngg.oauth.domain.discord;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.inflearngg.oauth.domain.OAuthProvider;
import org.inflearngg.oauth.domain.OAuthUserInfo;

@RequiredArgsConstructor
@AllArgsConstructor
public class DiscordUserInfo implements OAuthUserInfo {

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonProperty("email")
    private String email;

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public OAuthProvider getOAuthProvider() {
        return OAuthProvider.DISCORD;
    }

}
