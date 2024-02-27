package org.inflearngg.jwt.token;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.io.Decoders;

@Slf4j
@Component
public class JwtTokenProvider {
    private final Key key;

    public JwtTokenProvider(@Value("${jwt.secret-key}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String subject, Date expireDate) {
        return Jwts.builder()
                .setSubject(subject)
                .setExpiration(expireDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractSubject(String accessToken) {
        Claims claims = parseClaims(accessToken);
        return claims.getSubject();
    }

    public boolean validateToken(String accessToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            // SecurityException : JWT를 구문 분석 할 수 없거나 서명을 확인 할 수 없음
            log.warn("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            // ExpiredJwtException : JWT의 유효 기간이 만료 됨
            log.warn("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            // UnsupportedJwtException : JWT가 지원되지 않는 토큰형식
            log.warn("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            // JWT 내용이 비어 있음
            log.warn("JWT claims string is empty", e);
        }

        return false;
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
