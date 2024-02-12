package org.inflearngg.member.service;

import lombok.RequiredArgsConstructor;
import org.inflearngg.member.entity.Member;
import org.inflearngg.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public void saveUser(Member member) {
        memberRepository.save(member);
    }
}
