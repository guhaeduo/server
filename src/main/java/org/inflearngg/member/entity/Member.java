package org.inflearngg.member.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.inflearngg.duo.entity.DuoPost;
import org.inflearngg.summoner.entity.VerifySummoner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long memberId;
    private LocalDate createdAt = LocalDate.now();
    private String email;
    private String password;
    private String socialId;
    private boolean isRiotVerified;
//    @OneToMany(mappedBy = "member")
//    private List<RiotAccount> riotAccounts = new ArrayList<>();

    // 인증된소환사이름과 연관관계 매핑
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<VerifySummoner> verifySummonerList = new ArrayList<>();

    // 듀오게시판과도 연관관계매핑
    @OneToMany(mappedBy = "member")
    private List<DuoPost> duoPostList = new ArrayList<>();

    public Member(Long memberId){
        this.memberId = memberId;
    }

    public Member(String email, String password,String socialId){
        this(email, password, socialId, false);
    }

    public Member(String email, String password, String socialId, boolean isRiotVerified) {
        this.email = email;
        this.password = password;
        this.socialId = socialId;
        this.isRiotVerified = isRiotVerified;
    }
}
