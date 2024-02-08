package org.inflearngg.duo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.inflearngg.user.entity.User;

import java.time.LocalDate;

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
    private LocalDate createdAt = LocalDate.now();
    @Column(nullable = false)
    private boolean isRiotVerified;
    @Column(nullable = true)
    private String postPassword;
    private String riotGameName;
    private String riotGameTag;
    @Column(nullable = false)
    private String pUuid;
    private String needPosition;
    private String needQueueType;
    private String needTier;

    private String myMainLane;
    private String myMainChampionName;
    private int myMainChampionIconNumber;
    private String mySubLane;
    private String mySubChampionName;
    private int mySubChampionIconNumber;

    private boolean isMicOn;
    private String memo;
    // 유저랑 연관관계 매핑해야됨.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 테스트용 생성자
    public DuoPost(String riotGameName, String riotGameTag, String pUuid){
        this(false, null, riotGameName, riotGameTag, pUuid, "TOP", "SOLO", "IRON", "MID", "ZED", 238, "BOT", "KAI'SA", 145, true, "test");
    }

    // 테스트용 생성자
    public DuoPost(String riotGameName, String riotGameTag, String pUuid, boolean isRiotVerified) {
        this(isRiotVerified, null, riotGameName, riotGameTag, pUuid, "TOP", "SOLO", "IRON", "MID", "ZED", 238, "BOT", "KAI'SA", 145, true, "test");
    }

    public DuoPost(boolean isRiotVerified, String postPassword, String riotGameName, String riotGameTag, String pUuid, String needPosition, String needQueueType, String needTier, String myMainLane, String myMainChampionName, int myMainChampionIconNumber, String mySubLane, String mySubChampionName, int mySubChampionIconNumber, boolean isMicOn, String memo) {
        this.isRiotVerified = isRiotVerified;
        this.postPassword = postPassword;
        this.riotGameName = riotGameName;
        this.riotGameTag = riotGameTag;
        this.pUuid = pUuid;
        this.needPosition = needPosition;
        this.needQueueType = needQueueType;
        this.needTier = needTier;
        this.myMainLane = myMainLane;
        this.myMainChampionName = myMainChampionName;
        this.myMainChampionIconNumber = myMainChampionIconNumber;
        this.mySubLane = mySubLane;
        this.mySubChampionName = mySubChampionName;
        this.mySubChampionIconNumber = mySubChampionIconNumber;
        this.isMicOn = isMicOn;
        this.memo = memo;
    }

    // 추가되면, User에도 추가해야함.
    public void setUser(User user) {
        this.user = user;
        user.getDuoPostList().add(this);
    }
    // 제거하면 User에도 제거해야함.
    public void removeUser(User user) {
        user.getDuoPostList().remove(this);
        this.user = null;
    }
}
