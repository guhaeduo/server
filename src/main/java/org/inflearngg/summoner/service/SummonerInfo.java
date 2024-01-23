package org.inflearngg.summoner.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class SummonerInfo {


        @Getter
        @Setter
        @RequiredArgsConstructor
        public static class SummonerBasicInfo {
            private String id;
            private String puuid;
            private String name;
//            private String riotTag;
            private int summonerLevel;
            private int profileIconId;
        }




        @Getter
        @Setter
        @RequiredArgsConstructor
        public static class RankInfo {
            private String queueType;
//            tier => tier
            private String tier;
//             I=> 1, II =>2 , III => 3, IV =>4
            private String rank;
//            leaguePoints => point
            private int leaguePoints;
            private int wins;
            private int losses;
        }
}


