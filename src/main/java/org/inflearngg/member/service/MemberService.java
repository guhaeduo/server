package org.inflearngg.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.inflearngg.login.site.exception.EmailNotFoundException;
import org.inflearngg.member.dto.RiotAccount;
import org.inflearngg.member.entity.Member;
import org.inflearngg.member.exception.MemberIdNotFoundException;
import org.inflearngg.member.repository.MemberRepository;
import org.inflearngg.summoner.entity.VerifySummoner;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.inflearngg.aop.error.ExceptionErrorMessage.EMAIL_AND_PASSWORD_NOT_MATCH;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public void saveUser(Member member) {
        memberRepository.save(member);
    }

    public Member findUser(Long id) {
        return memberRepository.findById(id).orElseThrow();
    }

    public Long findUserIdByEmailOrNewMember(String email, String password, String socialId) {
        Optional<Member> member = memberRepository.findByEmailAndSocialId(email, socialId);
        if (member.isPresent()) {
            return member.get().getMemberId();
        }
        Member newMember = new Member(email, password, socialId);
        Member save = memberRepository.save(newMember);
        return save.getMemberId();
    }

    public Long findMemberIdByEmailAndPassword(String email, String password) {
        Optional<Member> member = memberRepository.findByEmailAndPasswordAndSocialId(email, password, "SITE");
        if (member.isPresent()) {
            return member.get().getMemberId();
        }
        throw new IllegalArgumentException(EMAIL_AND_PASSWORD_NOT_MATCH);
    }

    public boolean isSiteEmail(String email) {
        Optional<Member> member = memberRepository.findByEmailAndSocialId(email, "SITE");
        if (member.isPresent()) {
            return true;
        }
        return false;
    }

    public Long findMemberIdByEmail(String email) {
        Optional<Member> member = memberRepository.findByEmailAndSocialId(email, "SITE");
        if (member.isPresent()) {
            return member.get().getMemberId();
        }
        throw new EmailNotFoundException();
    }


    public Member updateMemberPassword(Long memberId, String password) {
        Optional<Member> member = memberRepository.findById(memberId);
        member.orElseThrow(EmailNotFoundException::new);
        if (!member.get().getSocialId().equals("SITE")) {
            throw new EmailNotFoundException();
        }
        member.get().setPassword(password);
        return memberRepository.save(member.get());
    }

    public void checkAndUpdateMemberPassword(Long memberId, String password, String newPassword) {
        Member member = findMemberByMemberId(memberId);
        // 비밀번호 확인
        checkMemberPassword(member, password);
        // 비밀번호 변경
        updateMemberPassword(memberId, newPassword);
    }

    public void checkMemberPassword(Member member, String password) {
        if (!member.getSocialId().equals("SITE")) {
            throw new EmailNotFoundException();
        }
        if (!member.getPassword().equals(password)) {
            throw new IllegalArgumentException(EMAIL_AND_PASSWORD_NOT_MATCH);
        }
    }

    public Member findMemberByMemberId(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(MemberIdNotFoundException::new);
    }


    public void saveRiotAccount(RiotAccount riotAccount, Long memberId) {
        VerifySummoner verifySummoner = new VerifySummoner();
        verifySummoner.setPuuid(riotAccount.getPuuid());
        verifySummoner.setSummonerName(riotAccount.getGameName());
        verifySummoner.setSummonerTag(riotAccount.getTagLine());

        Member member = findMemberByMemberId(memberId);
        verifySummoner.setMember(member);
        member.getVerifySummonerList().add(verifySummoner);
        memberRepository.save(member);
    }

    public void deleteMember(Long memberId ) {
        // 유저유무 체크
        Member member = findMemberByMemberId(memberId);
        // 삭제
        memberRepository.delete(member);
    }
}
