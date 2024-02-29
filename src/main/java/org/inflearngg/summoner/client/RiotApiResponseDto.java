package org.inflearngg.summoner.client;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class RiotApiResponseDto {

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
