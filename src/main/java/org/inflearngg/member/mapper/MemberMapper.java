package org.inflearngg.member.mapper;

import org.inflearngg.member.dto.RiotAccount;
import org.inflearngg.member.dto.response.MemberInfoDto;
import org.inflearngg.member.entity.Member;
import org.inflearngg.summoner.entity.VerifySummoner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MemberMapper {

    public MemberInfoDto memberToMemberInfoDto(Member member){
        MemberInfoDto memberInfoDto = new MemberInfoDto();
        memberInfoDto.setMemberId(member.getMemberId());
        memberInfoDto.setLoginType(member.getSocialId());
        memberInfoDto.setEmail(member.getEmail());
        memberInfoDto.setCreatedAt(member.getCreatedAt().toString());
        memberInfoDto.setRiotAccountList(riotAccountToRiotAccountList(member.getVerifySummonerList()));
        return memberInfoDto;
    }

    public List<RiotAccount> riotAccountToRiotAccountList(List<VerifySummoner> verifySummonerList){
        List<RiotAccount> riotAccountList = new ArrayList<>();
        for (VerifySummoner verifySummoner : verifySummonerList) {
            RiotAccount riotAccount = verifySummonerToRiotAccount(verifySummoner);
            riotAccountList.add(riotAccount);
        }
        return riotAccountList;
    }

    public RiotAccount verifySummonerToRiotAccount(VerifySummoner verifySummoner){
        RiotAccount riotAccount = new RiotAccount();
        riotAccount.setPuuid(verifySummoner.getPuuid());
        riotAccount.setGameName(verifySummoner.getSummonerName());
        riotAccount.setTagLine(verifySummoner.getSummonerTag());
        return riotAccount;
    }


}
