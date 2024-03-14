package org.inflearngg.login.jwt.token;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthTokenGenerator {
    private static final String BEARER_TYPE = "Bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME= 1000 * 60;            // 1분
    private static final long RESET_PASSWORD_TOKEN_EXPIRE_TIME= 1000 * 60* 30;            // 30분

    private static final long REFRESH_TOKEN_EXPIRE_TIME= 1000L * 60 * 60 * 24 * 365;         // 365일

    private final JwtTokenProvider jwtTokenProvider;

    public AuthToken generateAuthToken(Long userId) {
        long now = System.currentTimeMillis();
        Date accessTokenExpireDate = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        Date refreshTokenExpireDate = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);
        String subject = userId.toString();
        String accessToken = jwtTokenProvider.generateToken(subject, accessTokenExpireDate);
        // 사용자 정보를 담고 있지 않아야한다. (보안상의 이유)
        String refreshToken = jwtTokenProvider.generateToken(subject, refreshTokenExpireDate);
        return AuthToken.of(accessToken, refreshToken, BEARER_TYPE, ACCESS_TOKEN_EXPIRE_TIME);
    }

    //userId 추출 (long 타입일 경우)
    public Long extractUserId(String accessToken) {
        return Long.valueOf(jwtTokenProvider.extractSubject(accessToken));
    }

    //accessToken 연장
    public AuthToken refreshAccessToken(String refreshToken) {
        long now = System.currentTimeMillis();
        Date accessTokenExpireDate = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        long memberId = extractUserId(refreshToken);
        log.info("subject : {}", memberId);
        String accessToken = jwtTokenProvider.generateToken(String.valueOf(memberId), accessTokenExpireDate);
        return AuthToken.of(accessToken, "", BEARER_TYPE, ACCESS_TOKEN_EXPIRE_TIME);

    }

    public AuthToken resetPasswordToken(Long userId) {
        long now = System.currentTimeMillis();
        Date accessTokenExpireDate = new Date(now + RESET_PASSWORD_TOKEN_EXPIRE_TIME);
        String subject = userId.toString();
        String accessToken = jwtTokenProvider.generateToken(subject, accessTokenExpireDate);
        return AuthToken.of(accessToken, "", BEARER_TYPE, RESET_PASSWORD_TOKEN_EXPIRE_TIME);
    }
}
