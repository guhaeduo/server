package org.inflearngg.summoner.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


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
            private String tier;
            private String rank;
            private int leaguePoints;
            private int wins;
            private int losses;
        }
}


