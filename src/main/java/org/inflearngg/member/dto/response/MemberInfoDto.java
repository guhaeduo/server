package org.inflearngg.member.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.inflearngg.member.dto.RiotAccount;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MemberInfoDto {
    private Long memberId;
    private String loginType;
    private String email;
    private List<RiotAccount> riotAccountList;
    //created_at
    private String createdAt;

}
