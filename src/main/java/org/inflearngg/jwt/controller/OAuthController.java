package org.inflearngg.jwt.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.inflearngg.jwt.token.AuthToken;
import org.inflearngg.jwt.service.OAuthLoginService;
import org.inflearngg.oauth.client.login.kakao.KakaoLoginParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/oauth")
public class OAuthController {

    private final OAuthLoginService oAuthLoginService;

    @GetMapping("/kakao")
    public String getkakaoCode() {
        return oAuthLoginService.getAccessCode();
    }

    @PostMapping("/kakao")
    public ResponseEntity<AuthToken> kakaoLogin(@RequestBody KakaoLoginParam param) {
        log.info("code={}", param.getAuthorizeCode());
        AuthToken authToken = oAuthLoginService.login(param);
        return new ResponseEntity<>(authToken, HttpStatus.OK);
    }
}
