package org.inflearngg.jwt.service;

import lombok.RequiredArgsConstructor;
import org.inflearngg.jwt.token.AuthToken;
import org.inflearngg.jwt.token.AuthTokenGenerator;
import org.inflearngg.member.service.MemberService;
import org.inflearngg.oauth.client.login.OAuthLoginParams;
import org.inflearngg.oauth.domain.OAuthUserInfo;
import org.inflearngg.oauth.service.RequestOAuthInfoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class OAuthLoginService {

    private final AuthTokenGenerator authTokenGenerator;
    private final RequestOAuthInfoService requestOAuthInfoService;
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @Value("${oauth.kakao.client-id}")
    private String clientId;

    public String getAccessCode() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://kauth.kakao.com/oauth/authorize")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", "http://localhost:8080/api/oauth/kakao/callback")
                .queryParam("response_type", "code");
        return builder.toUriString();
    }

    public AuthToken login(OAuthLoginParams params) {
        OAuthUserInfo oAuthUserInfo = requestOAuthInfoService.request(params);
        String email = oAuthUserInfo.getEmail();
        String socialId = oAuthUserInfo.getOAuthProvider().name();
        // 유저 있는지 확인후, 있으면 userID를 받아오고, 없으면 만들고 userID를 받아온다.
        Long userId = memberService.findUserIdByEmailOrNewMember(email, passwordEncoder.encode(oAuthUserInfo.getEmail()), socialId);
        return authTokenGenerator.generateAuthToken(userId);
    }

}
