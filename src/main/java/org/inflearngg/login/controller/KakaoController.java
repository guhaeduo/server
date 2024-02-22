package org.inflearngg.login.controller;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.inflearngg.login.service.KakaoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("/api/login")
@RequiredArgsConstructor
public class KakaoController {

    private final KakaoService kakaoService;

    @GetMapping("/kakao")
    public String login() {
        log.info(kakaoService.getLoginUrl());
        return "redirect:" + kakaoService.getLoginUrl();
    }

    @GetMapping("/kakao/callback")
    public String callback(@RequestParam("code") String code, HttpSession session) {
        log.info("접근 code: " + code);
        String accessToken = kakaoService.getAccessToken(code);
        // 토큰 21599 6시간, 리플래시 토큰 5183999 60일
        log.info("accessToken: " + accessToken);
        session.setAttribute("accessToken", accessToken);

        String userInfo = kakaoService.getUserInfo(accessToken);
        log.info("userInfo: " + userInfo);

        return "redirect:/";
    }
}
