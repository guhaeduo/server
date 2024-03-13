package org.inflearngg.login.site.service;

import lombok.RequiredArgsConstructor;
import org.inflearngg.login.jwt.token.AuthToken;
import org.inflearngg.login.jwt.token.AuthTokenGenerator;
import org.inflearngg.member.entity.Member;
import org.inflearngg.member.service.MemberService;
import org.springframework.stereotype.Service;

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
        //중복체크
        if(isAccountEmail(email)){
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        Long memberId = memberService.findUserIdByEmailOrNewMember(email, password, "SITE");
        if (memberId == null)
            throw new IllegalArgumentException("회원가입에 실패하였습니다.");
    }

    public boolean isAccountEmail(String email) {
        return memberService.isSiteEmail(email);
    }

    public void resetPassword(String email, String password) {
        //이메일로 회원정보 조회
        Member siteEmail = memberService.updateMemberPassword(email, password);

        if (!siteEmail.getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호 변경에 실패하였습니다.");
        }
    }
}
