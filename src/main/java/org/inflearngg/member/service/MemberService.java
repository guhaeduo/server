package org.inflearngg.member.service;

import lombok.RequiredArgsConstructor;
import org.inflearngg.member.entity.Member;
import org.inflearngg.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public Member findUserByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow();
    }

    public Long findUserIdByEmailOrNewMember(String email, String password, String socialId) {
        Optional<Member> byEmail = memberRepository.findByEmail(email);
        if (byEmail.isPresent()) {
            return byEmail.get().getMemberId();
        }
        Member member = new Member(email, password, socialId);
        Member save = memberRepository.save(member);
        return save.getMemberId();

    }


}
