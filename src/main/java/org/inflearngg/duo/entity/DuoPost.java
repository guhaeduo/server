package org.inflearngg.duo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.inflearngg.member.entity.Member;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class DuoPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long postId;
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    private boolean isGuestPost;
    @Column(nullable = false)
    private boolean isRiotVerified;
    private String riotGameName;
    private String riotGameTag;
    //혹시 모르니까
    @Column(nullable = false)
    private String pUuid;
    private int profileIconId;
    // 원하는 포지션 및 티어
    private String needPosition;
    private String needQueueType;

    // 라인 및 챔프
    private String myMainLane;
    private String myMainChampionName;
    private String mySubLane;
    private String mySubChampionName;

    // 랭크 티어1~4
    private String mySoloRankTier;
    private String mySoloRankLevel;
    private String myFreeRankTier;
    private String myFreeRankLevel;

    private boolean isMicOn;
    private String memo;
    // 유저랑 연관관계 매핑해야됨.
    @Column(nullable = true)
    private String postPassword;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // 테스트용 생성자
    public DuoPost(String lane, String queueType, String tier) {
        this(false, "테스트", "kr", "테스트puuid", lane, queueType, "TOP", "Timo", "MID", "Ari", "GOLD", "IV", "SILVER", "II", true, "메모", "1234");
    }

    public DuoPost(String lane, String queueType, String tier, boolean isRiotVerified) {
        this(isRiotVerified, "테스트", "kr", "테스트puuid", lane, queueType, "TOP", "Timo", "MID", "Ari", "GOLD", "IV", "SILVER", "II", true, "메모", "1234");
        this.setMember(new Member(1L));
        this.isGuestPost = true;
    }
    public DuoPost(boolean isRiotVerified,String riotGameName, String riotGameTag, String pUuid, String needPosition, String needQueueType, String myMainLane, String myMainChampionName, String mySubLane, String mySubChampionName, String mySoloRankTier, String mySoloRankLevel, String myFreeRankTier, String myFreeRankLevel, boolean isMicOn, String memo, String postPassword) {
        this.isRiotVerified = isRiotVerified;
        this.riotGameName = riotGameName;
        this.riotGameTag = riotGameTag;
        this.pUuid = pUuid;
        this.needPosition = needPosition;
        this.needQueueType = needQueueType;

        this.myMainLane = myMainLane;
        this.myMainChampionName = myMainChampionName;
        this.mySubLane = mySubLane;
        this.mySubChampionName = mySubChampionName;

        this.mySoloRankTier = mySoloRankTier;
        this.mySoloRankLevel = mySoloRankLevel;
        this.myFreeRankTier = myFreeRankTier;
        this.myFreeRankLevel = myFreeRankLevel;

        this.isMicOn = isMicOn;
        this.memo = memo;
        this.postPassword = postPassword;
    }


    // 추가되면, User에도 추가해야함.
    public void setUser(Member member) {
        this.member = member;
        member.getDuoPostList().add(this);
    }
    // 제거하면 User에도 제거해야함.
    public void removeUser(Member member) {
        member.getDuoPostList().remove(this);
        this.member = null;
    }
}
