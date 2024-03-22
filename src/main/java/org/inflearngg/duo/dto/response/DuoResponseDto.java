package org.inflearngg.duo.dto.response;

import graphql.relay.PageInfo;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;


public class DuoResponseDto {

    private static final String zoneId = "UTC";//"Asia/Seoul";


    @Getter
    @Setter
    @NoArgsConstructor
    public static class PageInfo{
        List<DuoInfo> content;
        boolean hasNextPage;
        boolean hasPreviousPage;
        int currentPageNumber;

        int nextPageNumber;
        int previousPageNumber;

    }


    // 동적 쿼리로 나온 결과물
    @Getter
    @Setter
    @NoArgsConstructor
    public static class DuoInfo {
        private Boolean isGuestPost;
        private Long postId;
        private Long createdAt;
        private Long memberId = -1L; // -1L이면 비회원
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

        public DuoInfo(Boolean isGuestPost, Long postId, LocalDateTime createdAt, Long memberId, Integer summonerIconNumber, String riotGameName, String riotGameTag, String needPosition, String queueType, String myMainLane, String myMainChampionName, String mySubLane, String mySubChampionName, String soloRankTier, String soloRankLevel, String freeRankTier, String freeRankLevel, String memo, String pUuid, Boolean isMicOn, Boolean isRiotVerified) {
            this.isGuestPost = isGuestPost;
            this.postId = postId;
            this.createdAt = createdAt.atZone(ZoneId.of(zoneId)).toEpochSecond() * 1000;
            this.memberId = memberId;
            this.summonerIconNumber = summonerIconNumber;
            this.riotGameName = riotGameName;
            this.riotGameTag = riotGameTag;
            this.needPosition = needPosition;
            this.queueType = queueType;
            this.myMainLane = myMainLane;
            this.myMainChampionName = myMainChampionName;
            this.mySubLane = mySubLane;
            this.mySubChampionName = mySubChampionName;
            this.soloRankTier = soloRankTier;
            this.soloRankLevel = soloRankLevel;
            this.freeRankTier = freeRankTier;
            this.freeRankLevel = freeRankLevel;
            this.memo = memo;
            this.pUuid = pUuid;
            this.isMicOn = isMicOn;
            this.isRiotVerified = isRiotVerified;
        }
    }



}
