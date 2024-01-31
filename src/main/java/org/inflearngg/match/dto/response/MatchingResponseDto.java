package org.inflearngg.match.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MatchingResponseDto {
    private SummonerRankInfo summonerRankInfo;
    private List<MatchData> matchDataList;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SummonerRankInfo{
        private RankInfo info;
        private Lane lane;

        @Getter
        @Setter
        @AllArgsConstructor
        @NoArgsConstructor
        public static class RankInfo{
            private double winningRate;
            private int wins;
            private int loses;

            private double kda;
            private int killAvg;
            private int deathAvg;
            private int assistAvg;


        }
        @Getter
        @Setter
        @NoArgsConstructor
        public static class Lane{
            private RankLaneData all;
            private RankLaneData top;
            private RankLaneData jug;
            private RankLaneData mid;
            private RankLaneData adc;
            private RankLaneData sup;

            @Getter
            @Setter
            @NoArgsConstructor
            @AllArgsConstructor
            public static class RankLaneData{
                private double winningRate;
                private List<MostChampion> mostChampionlist;


                @Getter
                @Setter
                @NoArgsConstructor
                @AllArgsConstructor
                public static class MostChampion{
                    private String championName;
                    private int championIconNumber;
                    private double winningRate;
                    private double csPerMinute;
                    private double visionScorePerMinute;
                    private double kda;
                    private double totalKillParticipation;
                }
            }
        }
    }


    @Getter
    @Setter
    @NoArgsConstructor
    public static class MatchData {
        private MatchInfo info;
        private SummonerMatchData currentSummonerMatchData;
        private Team red;
        private Team blue;

        @Getter
        @Setter
        @NoArgsConstructor
        public static class MatchInfo {
            private int gameDuration;
            private int gameEndStamp;
            private String queueType;
            private boolean quickShutdown;
            private MaxDamage maxDamage;

            // getters and setters

            public static class MaxDamage{
                private int damage;
                private String championName;
                private int championIconNumber;

                private String summonerName;
                private String summonerTag;
            }
        }
        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class SummonerMatchData {
            private String position;
            private int kill;
            private boolean isBot;
            private int death;
            private int assists;
            private int damage;
            private int minionKill;
            private String championName;
            private int championIconNumber;
            private int championLevel;
            private String riotName;
            private String riotTag;
            private int totalDamage;
            private int totalGold;
            private List<Integer> itemNumberList;
            private Perks perks;
            private int visionWards;
            private int visionScore;
            private int wardPlaced;
            private int spell1Id;
            private int spell2Id;
            private int pUuid;

            // getters and setters
            @Getter
            @Setter
            public static class Perks {
                private PerkDetail main;
                private PerkDetail sub;

                // getters and setters
                @Getter
                @Setter
                @NoArgsConstructor
                public static class PerkDetail {
                    private int perkStyle;
                    private List<Integer> perkIdList;
                }
            }
        }


        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Team {
            private int totalKill;
            private int totalDeath;
            private int totalAssists;
            private int totalGold;
            private int teamMaxDamage;
            private Objectives objectives;
            private List<SummonerMatchData> participants;
            private boolean isWin;

            //nullable
            private Integer grade;

            // getters and setters

            @Getter
            @Setter
            @NoArgsConstructor
            @AllArgsConstructor
            public static class Objectives {
                private int baron;
                private int dragon;
                private int horde;
                private int inhibitor;
                private int riftHerald;
                private int tower;

                // getters and setters
            }

        }


    }


    





}
