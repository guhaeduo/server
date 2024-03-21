package org.inflearngg.duo.dto.response;

import lombok.*;

import java.time.LocalDate;


public class DuoResponseDto {


    // 동적 쿼리로 나온 결과물
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DuoInfo {
        private Boolean isGuestPost;
        private Long postId;
        private Long createdAt;
        private Integer summonerIconNumber; //유저 아이콘 번호

        private String riotGameName;
        private String riotGameTag;
        private String needPosition;
        private String queueType;

        private String myMainLane;
        private String myMainChampionName;
        private String mySubLane;
        private String mySubChampionName;

        private String soloRankTier;
        private String soloRankLevel; // 안쓰지만 혹시 몰라
        private String freeRankTier;
        private String freeRankLevel; // 안쓰지만 혹시 몰라

        private String memo;
        private String pUuid;
        private Boolean isMicOn;
        private Boolean isRiotVerified;
    }


}
