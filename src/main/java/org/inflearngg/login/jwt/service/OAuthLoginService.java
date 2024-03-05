package org.inflearngg.login.jwt.service;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.inflearngg.login.jwt.token.AuthToken;
import org.inflearngg.login.jwt.token.AuthTokenGenerator;
import org.inflearngg.login.jwt.token.JwtErrorMessage;
import org.inflearngg.login.jwt.token.JwtTokenProvider;
import org.inflearngg.member.service.MemberService;
import org.inflearngg.login.oauth.accesstoken.OAuthAccessCode;
import org.inflearngg.login.oauth.client.login.OAuthLoginParams;
import org.inflearngg.login.oauth.domain.OAuthUserInfo;
import org.inflearngg.login.oauth.service.RequestOAuthInfoService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthLoginService {

    private final AuthTokenGenerator authTokenGenerator;
    private final RequestOAuthInfoService requestOAuthInfoService;
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;


    public String getAccessCode(OAuthAccessCode code) {
        return requestOAuthInfoService.requestAccessCode(code);
    }


    public AuthToken login(OAuthLoginParams params) {
        OAuthUserInfo oAuthUserInfo = requestOAuthInfoService.request(params);
        String email = oAuthUserInfo.getEmail();
        String socialId = oAuthUserInfo.getOAuthProvider().name();
        // 유저 있는지 확인후, 있으면 userID를 받아오고, 없으면 만들고 userID를 받아온다.
        Long userId = memberService.findUserIdByEmailOrNewMember(email, passwordEncoder.encode(oAuthUserInfo.getEmail()), socialId);
        log.info("userId : {}", userId);
        AuthToken authToken = authTokenGenerator.generateAuthToken(userId);
        authToken.setMemberId(userId);
        return authToken;
    }

    public AuthToken refreshAccessToken(String refreshToken) {
        //검증
        if (StringUtils.hasText(refreshToken) && jwtTokenProvider.validateToken(refreshToken)) {
            return authTokenGenerator.refreshAccessToken(refreshToken);
        }
        throw new JwtException(JwtErrorMessage.UNKNOWN_ERROR.getMessage());
    }
}
