package org.inflearngg.jwt.token;

import lombok.Getter;

@Getter
public class AuthToken {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Long expiresIn;

    public AuthToken(String accessToken, String refreshToken, String tokenType, Long expiresIn) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
    }

    public static AuthToken of(String accessToken, String refreshToken, String tokenType, Long expiresIn) {
        return new AuthToken(accessToken, refreshToken, tokenType, expiresIn);
    }
}
