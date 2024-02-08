package org.inflearngg.user.entity;

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
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long userId;
    private LocalDate createdAt = LocalDate.now();
    private String email;
    private String password;
    private boolean isRiotVerified;
    // 인증된소환사이름과 연관관계 매핑
    @OneToMany(mappedBy = "user")
    private List<VerifySummoner> verifySummonerList = new ArrayList<>();

    // 듀오게시판과도 연관관계매핑
    @OneToMany(mappedBy = "user")
    private List<DuoPost> duoPostList = new ArrayList<>();

    public User(Long userId){
        this.userId = userId;
    }

}
