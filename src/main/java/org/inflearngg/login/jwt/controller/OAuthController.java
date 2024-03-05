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
import org.springframework.util.MultiValueMap;
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
    public ResponseEntity<LoginResponseDto> kakaoLogin(
            @RequestBody KakaoLoginParam param) {
        log.info("code={}", param.getAuthorizeCode());
        AuthToken authToken = oAuthLoginService.login(param);
        //accessToken, refreshToken, expiresIn 을 헤더에 담아서 보내준다.

        return new ResponseEntity<>(loginMapper.mapToAuthToken(authToken), setTokenHttpHeaders(authToken), HttpStatus.OK);
    }


    @PostMapping("/discord")
    public ResponseEntity<LoginResponseDto> discordLogin(@RequestBody DiscordLoginParam param) {
        log.info("code={}, provider={} ", param.getAuthorizeCode(), param.oAuthProvider());
        AuthToken authToken = oAuthLoginService.login(param);
        return new ResponseEntity<>(loginMapper.mapToAuthToken(authToken), setTokenHttpHeaders(authToken), HttpStatus.OK);
    }

    @NotNull
    private static MultiValueMap<String, String> setTokenHttpHeaders(AuthToken authToken) {
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add("accessToken", authToken.getAccessToken());
        headers.add("refreshToken", authToken.getRefreshToken());
        headers.add("expiresIn", String.valueOf(authToken.getExpiresIn()));
        return headers;
    }
}
