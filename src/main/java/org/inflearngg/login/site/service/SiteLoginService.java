package org.inflearngg.login.site.service;

import lombok.RequiredArgsConstructor;
import org.inflearngg.login.jwt.token.AuthToken;
import org.inflearngg.login.jwt.token.AuthTokenGenerator;
import org.inflearngg.login.site.exception.EmailNotFoundException;
import org.inflearngg.login.site.exception.ExistingEmailException;
import org.inflearngg.member.entity.Member;
import org.inflearngg.member.service.MemberService;
import org.springframework.stereotype.Service;

import static org.inflearngg.aop.error.ExceptionErrorMessage.PASSWORD_RESET_FAIL;

@Service
@RequiredArgsConstructor
public class SiteLoginService {

    private final MemberService memberService;
    private final AuthTokenGenerator authTokenGenerator;


    public AuthToken login(String email, String password) {
        //db에서 email로 회원정보 조회
        Long memberId = memberService.findMemberIdByEmailAndPassword(email, password);
        //일치하면 토큰 발급
        AuthToken authToken = authTokenGenerator.generateAuthToken(memberId);
//토큰 발급 로직은 jwt를 사용하여 구현
        return authToken;
    }


    public void signUp(String email, String password) {
        memberService.findUserIdByEmailOrNewMember(email, password, "SITE");
    }

    //중복 체크
    public void checkEmailDuplication(String email) {
        if (memberService.isSiteEmail(email)) {
            throw new ExistingEmailException();
        }
    }

    public String makeResetPasswordJwtCode(String email) {
        //이메일로 회원정보 조회
        Long memberId = memberService.findMemberIdByEmail(email);
        //일치하면 토큰 발급
        AuthToken authToken = authTokenGenerator.resetPasswordToken(memberId);
        return authToken.getAccessToken();
    }


    public void resetPassword(Long memberId, String password) {
        //이메일로 회원정보 조회
        Member siteEmail = memberService.updateMemberPassword(memberId, password);

        if (!siteEmail.getPassword().equals(password)) {
            throw new IllegalArgumentException(PASSWORD_RESET_FAIL);
        }
    }

    public void updatePassword(Long memberId, String beforePassword, String afterPassword) {
        memberService.checkAndUpdateMemberPassword(memberId, beforePassword, afterPassword);
    }
}
