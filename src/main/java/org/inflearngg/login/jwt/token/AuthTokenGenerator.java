package org.inflearngg.login.jwt.token;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class AuthTokenGenerator {
    private static final String BEARER_TYPE = "Bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME= 1000 * 60 * 30;            // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME= 1000 * 60 * 60 * 24 * 7;  // 7일

    private final JwtTokenProvider jwtTokenProvider;

    public AuthToken generateAuthToken(Long userId) {
        long now = System.currentTimeMillis();
        Date accessTokenExpireDate = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        Date refreshTokenExpireDate = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);
        String subject = userId.toString();
        String accessToken = jwtTokenProvider.generateToken(subject, accessTokenExpireDate);
        String refreshToken = jwtTokenProvider.generateToken(subject, refreshTokenExpireDate);
        return AuthToken.of(accessToken, refreshToken, BEARER_TYPE, ACCESS_TOKEN_EXPIRE_TIME);
    }

    //userId 추출 (long 타입일 경우)
    public Long extractUserId(String accessToken) {
        return Long.valueOf(jwtTokenProvider.extractSubject(accessToken));
    }

}
