package org.inflearngg.summoner.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.inflearngg.user.entity.User;

@Entity
@Setter @Getter
@NoArgsConstructor
public class VerifySummoner {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long verifySummonerId;
    private String summonerName;
    private String summonerTag;

    // 유저랑 연관관계 매핑해야됨.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 추가되면, User에도 추가해야함.



}
