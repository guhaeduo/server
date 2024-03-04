package org.inflearngg.login.jwt.token;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum JwtErrorMessage {
    UNKNOWN_ERROR(HttpStatus.UNAUTHORIZED,"인증 토큰이 존재하지 않습니다."),
    WRONG_TYPE_TOKEN(HttpStatus.UNAUTHORIZED,"잘못된 토큰 정보입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED,"만료된 토큰 정보입니다."),
    UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED,"지원하지 않는 토큰 방식입니다."),
    ACCESS_DENIED(HttpStatus.UNAUTHORIZED,"알 수 없는 이유로 요청이 거절되었습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
