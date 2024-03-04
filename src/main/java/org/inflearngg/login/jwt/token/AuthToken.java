package org.inflearngg.login.jwt.token;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthToken {
    private long memberId;
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
