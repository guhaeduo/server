package org.inflearngg.login.site.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.inflearngg.login.jwt.dto.LoginResponseDto;
import org.inflearngg.login.jwt.token.AuthToken;
import org.inflearngg.login.oauth.accesstoken.KakaoAccessCode;
import org.inflearngg.login.oauth.client.login.kakao.KakaoLoginParam;
import org.inflearngg.login.site.dto.request.EmailAuthenticationDto;
import org.inflearngg.login.site.dto.request.EmailCodeVerifyDto;
import org.inflearngg.login.site.dto.request.ResetPasswordDto;
import org.inflearngg.login.site.dto.request.SiteLoginMemberInfo;
import org.inflearngg.login.site.service.MailService;
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
@RequestMapping("/api/site")
@CrossOrigin(exposedHeaders = {"access-token", "refresh-token", "expiresIn", "token-type"})
public class SiteLoginController {

    private static final String BEARER_TYPE = "Bearer";
    private final SiteLoginService siteLoginService;
    private final MailService mailService;


    // 자체 로그인
    @PostMapping("/login")
    public ResponseEntity siteLogin(
            @RequestBody SiteLoginMemberInfo siteLoginMemberInfo) {
        AuthToken authToken = siteLoginService.login(siteLoginMemberInfo.getEmail(), siteLoginMemberInfo.getPassword());
        return new ResponseEntity<>(setTokenHttpHeaders(authToken), HttpStatus.OK);
    }

      //  자체 회원가입
    @PostMapping("/signup")
    public ResponseEntity siteSignUp(
            @RequestBody SiteLoginMemberInfo siteLoginMemberInfo) {
        if(siteLoginService.signUp(siteLoginMemberInfo.getEmail(), siteLoginMemberInfo.getPassword())){
            return new ResponseEntity<>("회원가입완료", HttpStatus.OK);
        }
        throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
    }

    // 자체 비밀번호 찾기
    @PostMapping("/find-password")
    public ResponseEntity findPassword(
            @RequestBody EmailAuthenticationDto emailReq) throws Exception {
        // 이메일이 존재하는지 체크 후 인증코드를 보낸다.
        if(!siteLoginService.isAccountEmail(emailReq.getEmail()))
            return new ResponseEntity<>("이메일이 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        String passwordReSetUri = mailService.getPasswordReSetUri(emailReq.getEmail());
        return new ResponseEntity<>(passwordReSetUri, HttpStatus.OK);
    }
    @PatchMapping("/reset-password")
    public ResponseEntity resetPassword(
            @RequestBody ResetPasswordDto resetPasswordDto) {
        String s = mailService.verifyEmailCode(resetPasswordDto.getEmail(), resetPasswordDto.getCode());// 핸들링 필요
        siteLoginService.resetPassword(resetPasswordDto.getEmail(),resetPasswordDto.getPassword());
        return new ResponseEntity<>("비밀번호 변경 완료", HttpStatus.OK);
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


    @PostMapping("/email-code/request")
    public String mailAuthentication(@Valid @RequestBody EmailAuthenticationDto emailReq) throws Exception {
        return mailService.sendCertificationMail(emailReq.getEmail());
    }

    @GetMapping("/email-code/verify")
    public String verifyEmailCode(@Valid EmailCodeVerifyDto dto) {
        return mailService.verifyEmailCode(dto.getEmail(), dto.getCode());
    }



}
