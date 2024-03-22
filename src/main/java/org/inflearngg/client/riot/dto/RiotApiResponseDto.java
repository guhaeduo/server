package org.inflearngg.client.riot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.inflearngg.duo.dto.QueueType;

public class RiotApiResponseDto {


    @Getter
    @Setter
    @NoArgsConstructor
    public static class RiotPuuidAndTierInfo {
        private String puuid;
        private int profileIconId;
        private RankData soloRank = new RankData();
        private RankData freeRank = new RankData();

        @Setter
        @Getter
        @NoArgsConstructor
        public static class RankData{
            private String tier = "UNRANKED"; // gold, silver, bronze, platinum, diamond, master, grandmaster, challenger
            private String rank = "0"; // 1,2,3,4
            private QueueType queueType;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class RiotSummonerInfo {
        private String gameName;
        private String tagLine;
        private String summonerId;
        private String accountId;
        private String puuid;
        private int profileIconId;
        private long revisionDate;
        private int summonerLevel;
    }
    @Getter
    @Setter
    @NoArgsConstructor
    public static class RiotPuuid {
        private String puuid;
        private String gameName;
        private String tagLine;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class RiotSummonerIdInfo {
        private String id;
        private String accountId;
        private String puuid;
        private String name;
        private int profileIconId;
        private long revisionDate;
        private int summonerLevel;
    }
}
