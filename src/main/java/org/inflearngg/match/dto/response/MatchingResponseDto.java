package org.inflearngg.match.dto.response;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MatchingResponseDto {
    private int totalGameCnt;
    private String queueType;
    private List<MatchData> matchDataList;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SummonerRankInfo {
        private RankInfo info;
        private Lane lane;

        @Getter
        @Setter
        @AllArgsConstructor
        @NoArgsConstructor
        public static class RankInfo {
            private int cntGame;
            private String winningRate;
            private int wins;
            private int loses;

            private String kda;
            private int killAvg;
            private int deathAvg;
            private int assistAvg;
            private String mainLane;
            private String subLane;

            public void setWinningRate(double winningRate) {
                this.winningRate = Math.round(winningRate) + "%";
            }

            public void setKda(double kda) {
                this.kda = String.format("%.2f", kda);
            }

        }

        @Getter
        @Setter
        @NoArgsConstructor
        public static class Lane {
            @JsonSetter("ALL")
            private RankLaneData all;
            @JsonSetter("TOP")
            private RankLaneData top;
            @JsonSetter("JUG")
            private RankLaneData jug;
            @JsonSetter("MID")
            private RankLaneData mid;
            @JsonSetter("ADC")
            private RankLaneData adc;
            @JsonSetter("SUP")
            private RankLaneData sup;

            @Getter
            @Setter
            @NoArgsConstructor
            public static class RankLaneData {
                private int cntGame;
                private String winningRate;
                private List<MostChampion> mostChampionlist;

                public void setWinningRate(double winningRate) {
                    this.winningRate = Math.round(winningRate) + "%";
                }


                @Getter
                @Setter
                @NoArgsConstructor
                public static class MostChampion {
                    private int cntGame;
                    private String championName;
                    private String winningRate;
                    private String csPerMinute;
                    private String visionScore;
                    private String kda;
                    private String KillParticipation;


                    public void setWinningRate(double winningRate) {
                        this.winningRate = Math.round(winningRate) + "%";
                    }


                    public void setCsPerMinute(double csPerMinute) {
                        this.csPerMinute = String.format("%.2f", csPerMinute);
                    }

                    public void setVisionScore(double visionScorePerMinute) {
                        this.visionScore = String.format("%.2f", visionScorePerMinute);
                    }

                    public void setKda(double kda) {
                        this.kda = String.format("%.2f", kda);
                    }

                    public void setKillParticipation(double avgKillParticipation) {
                        this.KillParticipation = Math.round(avgKillParticipation) + "%";
                    }


                }
            }
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class MatchData {
        private String matchId;
        private MatchInfo info;
        private SummonerMatchData currentSummonerMatchData;
        private Team red;
        private Team blue;

        @Getter
        @Setter
        @NoArgsConstructor
        public static class MatchInfo {
            private int gameDuration;
            private long gameEndStamp;
            private String queueType;
            private boolean quickShutdown;
            private MaxData maxData;

            // getters and setters
            public void setQueueType(int queueType) {
                switch (queueType) {
                    case 420:
                        this.queueType = "솔로랭크";
                        break;
                    case 490:
                        this.queueType = "빠른대전(일반게임)";
                        break;
                    case 440:
                        this.queueType = "자유랭크";
                        break;
                    case 450:
                        this.queueType = "무작위 총력전";
                        break;
                    case 840:
                        this.queueType = "봇전(초급)";
                        break;
                    case 850:
                        this.queueType = "봇전(중급)";
                        break;
                    case 1900:
                        this.queueType = "우르프";
                        break;
                    default:
                        this.queueType = "기타";
                        break;
                }
            }

            @Getter
            @Setter
            @NoArgsConstructor
            public static class MaxData {
                private MaxDamage maxDamage;
                private MaxKill maxKill;
                private MaxDeath maxDeath;
                private MaxAssist maxAssist;

                @Getter
                @Setter
                public static class MaxDamage {
                    private int damage;
                    private String championName;
                    private String riotGameName;

                    public void setMaxDamage(int damage, String riotGameName, String championName) {
                        this.damage = damage;
                        this.riotGameName = riotGameName;
                        this.championName = championName;
                    }
                }

                @Getter
                @Setter
                public static class MaxKill {
                    private int kill;
                    private String championName;
                    private String riotGameName;

                    public void setMaxKill(int kill, String riotGameName, String championName) {
                        this.kill = kill;
                        this.riotGameName = riotGameName;
                        this.championName = championName;
                    }
                }

                @Getter
                @Setter
                public static class MaxDeath {
                    private int death;
                    private String championName;
                    private String riotGameName;

                    public void setMaxDeath(int death, String riotGameName, String championName) {
                        this.death = death;
                        this.riotGameName = riotGameName;
                        this.championName = championName;
                    }
                }

                @Getter
                @Setter
                public static class MaxAssist {
                    private int assist;
                    private String championName;
                    private String riotGameName;

                    public void setMaxAssist(int assist, String riotGameName, String championName) {
                        this.assist = assist;
                        this.riotGameName = riotGameName;
                        this.championName = championName;
                    }
                }

            }
        }

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class SummonerMatchData {
            private String lane;
            private boolean isWin;
            private int kill;
            private boolean isBot;
            private int death;
            private int assists;
            private int minionKill;
            private String killParticipation;
            private String csPerMinute;
            private String championName;
            private int championLevel;
            private String riotGameName;
            private String riotGameTag;
            private int totalDamage;
            private int totalGold;
            private List<Integer> itemNumberList;
            private Perks perks;
            private int visionWards;
            private int visionScore;
            private int wardPlaced;
            private int spell1Id;
            private int spell2Id;
            private String pUuid;

            // getters and setters

            public void setKillParticipation(double killParticipation) {
                this.killParticipation = Math.round(killParticipation) + "%";
            }

            public void setCsPerMinute(double csPerMinute) {
                this.csPerMinute = String.format("%.1f", csPerMinute);
            }

            @Getter
            @Setter
            @NoArgsConstructor
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
        public static class Team {
            private int totalKills;
            private int totalDeaths;
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
