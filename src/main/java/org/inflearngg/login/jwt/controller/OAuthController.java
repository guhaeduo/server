package org.inflearngg.login.jwt.controller;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.inflearngg.login.jwt.dto.LoginResponseDto;
import org.inflearngg.login.jwt.mapper.LoginMapper;
import org.inflearngg.login.jwt.token.AuthToken;
import org.inflearngg.login.jwt.service.OAuthLoginService;
import org.inflearngg.login.oauth.accesstoken.DiscordAccessCode;
import org.inflearngg.login.oauth.accesstoken.KakaoAccessCode;
import org.inflearngg.login.oauth.client.login.discord.DiscordLoginParam;
import org.inflearngg.login.oauth.client.login.kakao.KakaoLoginParam;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/oauth")
public class OAuthController {

    private final OAuthLoginService oAuthLoginService;
    private final LoginMapper loginMapper;
    @GetMapping("/kakao")
    public String getkakaoCode() {
        KakaoAccessCode kakaoAccessCode = new KakaoAccessCode();
        return oAuthLoginService.getAccessCode(kakaoAccessCode);
    }

    @GetMapping("/discord")
    public String getDiscordCode() {
        DiscordAccessCode discordAccessCode = new DiscordAccessCode();
        return oAuthLoginService.getAccessCode(discordAccessCode);
    }


    @PostMapping("/kakao")
    public ResponseEntity<LoginResponseDto> kakaoLogin(@RequestBody KakaoLoginParam param) {
        log.info("code={}", param.getAuthorizeCode());
        AuthToken authToken = oAuthLoginService.login(param);
        HttpHeaders headers = setCookieHttpOnlyHeaders(authToken);
        return new ResponseEntity<>(loginMapper.mapToAuthToken(authToken), headers, HttpStatus.OK);
    }



    @PostMapping("/discord")
    public ResponseEntity<LoginResponseDto> discordLogin(@RequestBody DiscordLoginParam param) {
        log.info("code={}, provider={} ", param.getAuthorizeCode(), param.oAuthProvider());
        AuthToken authToken = oAuthLoginService.login(param);
        HttpHeaders headers = setCookieHttpOnlyHeaders(authToken);
        return new ResponseEntity<>(loginMapper.mapToAuthToken(authToken),headers, HttpStatus.OK);
    }

    @NotNull
    private static HttpHeaders setCookieHttpOnlyHeaders(AuthToken authToken) {
        Cookie cookie = new Cookie("jwt", authToken.getAccessToken());
        cookie.setHttpOnly(true);  // HTTP-only 설정
        cookie.setSecure(true);  // HTTPS에서만 전송되도록 설정
        cookie.setPath("/");  // 쿠키의 유효 경로 설정
        // 쿠키를 문자열로 변환
        String cookieString = String.format("%s=%s; HttpOnly; Secure; Path=%s", cookie.getName(), cookie.getValue(), cookie.getPath());
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, cookieString);
        return headers;
    }
}
