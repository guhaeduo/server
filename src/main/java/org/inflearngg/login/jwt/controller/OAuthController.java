package org.inflearngg.login.jwt.controller;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.inflearngg.login.jwt.dto.LoginResponseDto;
import org.inflearngg.login.jwt.dto.RefreshToken;
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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;



@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/oauth")
@CrossOrigin(exposedHeaders = {"access-token", "refresh-token", "expiresIn", "token-type"})
public class OAuthController {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TYPE = "Bearer";

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

        return new ResponseEntity<>(setTokenHttpHeaders(authToken), HttpStatus.OK);
    }


    @PostMapping("/discord")
    public ResponseEntity discordLogin(@RequestBody DiscordLoginParam param) {
        log.info("code={}, provider={} ", param.getAuthorizeCode(), param.oAuthProvider());
        AuthToken authToken = oAuthLoginService.login(param);
        return new ResponseEntity<>(setTokenHttpHeaders(authToken), HttpStatus.OK);
    }

    //토큰 재발급
    @PostMapping("/refresh")
    public ResponseEntity refreshToken(@RequestBody RefreshToken refreshToken) {
        log.info("refreshToken={}", refreshToken.getRefreshToken());
        //refreshToken을 이용해서 새로운 토큰을 발급받는다.
        String s = resolveToken(refreshToken.getRefreshToken());
        AuthToken authToken = oAuthLoginService.refreshAccessToken(s);
        return new ResponseEntity<>(setTokenHttpHeaders(authToken), HttpStatus.OK);
    }



    @NotNull
    private static MultiValueMap<String, String> setTokenHttpHeaders(AuthToken authToken) {
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add("access-token", authToken.getAccessToken());
        if(authToken.getRefreshToken() != "")
            headers.add("refresh-token", authToken.getRefreshToken());
        headers.add("token-type", authToken.getTokenType());
        headers.add("expiresIn", String.valueOf(authToken.getExpiresIn()));
        return headers;
    }

    private String resolveToken(String bearerToken) {
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_TYPE)) {
            return bearerToken.substring(7);
        }
        throw new JwtException("잘못된 토큰 정보입니다.");
    }

}
