package org.inflearngg.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.inflearngg.member.dto.RiotAccount;
import org.inflearngg.member.dto.request.PasswordDto;
import org.inflearngg.member.dto.response.MemberInfoDto;
import org.inflearngg.member.mapper.MemberMapper;
import org.inflearngg.member.service.MemberService;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;
    private final MemberMapper memberMapper;

    @GetMapping("/info")
    public MemberInfoDto getMemberInfo(@RequestAttribute("memberId") String memberId) {
        log.info("memberId : {}", memberId);
        return memberMapper.memberToMemberInfoDto(
                memberService.findMemberByMemberId(
                        Long.valueOf(memberId)
                )
        );
    }

    //회원 삭제
    @DeleteMapping("/delete")
    public boolean deleteMember(@RequestAttribute("memberId") String memberId) {
        log.info("memberId : {}", memberId);
        memberService.deleteMember(Long.valueOf(memberId));
        return true;
    }


    //인증된 소환사 정보 넣기

    @PostMapping("/riotAccount/add")
    public boolean addRiotAccount(@RequestAttribute("memberId") String memberId,
                                  @RequestBody RiotAccount riotAccount) {
        log.info("memberId : {}", memberId);
        memberService.saveRiotAccount(riotAccount, Long.valueOf(memberId));
        return true;
    }
}
