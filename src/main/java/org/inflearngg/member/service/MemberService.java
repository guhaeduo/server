package org.inflearngg.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.inflearngg.member.dto.RiotAccount;
import org.inflearngg.member.entity.Member;
import org.inflearngg.member.repository.MemberRepository;
import org.inflearngg.summoner.entity.VerifySummoner;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        //비밀번호 일치하지 않을때
        throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
    }

    public boolean isSiteEmail(String email) {
        Optional<Member> member = memberRepository.findByEmailAndSocialId(email, "SITE");
        if (member.isPresent()) {
            return true;
        }
        return false;
    }
    public Member updateMemberPassword(String email, String password) {
        Optional<Member> member = memberRepository.findByEmailAndSocialId(email, "SITE");
        member.orElseThrow(() -> new IllegalArgumentException("회원으로 존재하지않는 이메일입니다."));
        member.get().setPassword(password);
        return memberRepository.save(member.get());
    }

    public Member findUserByMemberId(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. memberID : " + memberId));
    }


    public void saveRiotAccount(RiotAccount riotAccount, Long memberId) {
        VerifySummoner verifySummoner = new VerifySummoner();
        verifySummoner.setPuuid(riotAccount.getPuuid());
        verifySummoner.setSummonerName(riotAccount.getGameName());
        verifySummoner.setSummonerTag(riotAccount.getTagLine());

        Member member = findUserByMemberId(memberId);
        verifySummoner.setMember(member);
        member.getVerifySummonerList().add(verifySummoner);
        memberRepository.save(member);
    }
}
