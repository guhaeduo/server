package org.inflearngg.login.oauth.client.login.discord;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DiscordToken(
        @JsonProperty("token_type")
        String tokenType,
        @JsonProperty("access_token")
        String accessToken,
        @JsonProperty("expires_in")
        String expiresIn,
        @JsonProperty("refresh_token")
        String refreshToken,
        @JsonProperty("scope")
        String scope
) {
}
