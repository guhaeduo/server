package org.inflearngg.login.site.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.inflearngg.login.jwt.dto.LoginResponseDto;
import org.inflearngg.login.jwt.token.AuthToken;
import org.inflearngg.login.oauth.accesstoken.KakaoAccessCode;
import org.inflearngg.login.oauth.client.login.kakao.KakaoLoginParam;
import org.inflearngg.login.site.dto.request.SiteLoginMemberInfo;
import org.inflearngg.login.site.service.SiteLoginService;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/login")
@CrossOrigin(exposedHeaders = {"access-token", "refresh-token", "expiresIn", "token-type"})
public class SiteLoginController {

    private static final String BEARER_TYPE = "Bearer";
    private final SiteLoginService siteLoginService;




    @PostMapping("/site")
    public ResponseEntity<LoginResponseDto> siteLogin(
            @RequestBody SiteLoginMemberInfo siteLoginMemberInfo) {
        AuthToken authToken = siteLoginService.login(siteLoginMemberInfo.getEmail(), siteLoginMemberInfo.getPassword());
        //accessToken, refreshToken, expiresIn 을 헤더에 담아서 보내준다.
        return new ResponseEntity<>(setTokenHttpHeaders(authToken), HttpStatus.OK);
    }



    @NotNull
    private static MultiValueMap<String, String> setTokenHttpHeaders(AuthToken authToken) {
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add("access-token", authToken.getAccessToken());
        if (authToken.getRefreshToken() != "")
            headers.add("refresh-token", authToken.getRefreshToken());
        headers.add("token-type", authToken.getTokenType());
        headers.add("expiresIn", String.valueOf(authToken.getExpiresIn()));
        return headers;
    }
}
