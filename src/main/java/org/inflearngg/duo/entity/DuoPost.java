package org.inflearngg.duo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Setter
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
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

}
