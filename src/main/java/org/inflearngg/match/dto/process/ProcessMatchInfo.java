package org.inflearngg.match.dto.process;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.inflearngg.match.dto.MaxStat;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProcessMatchInfo {

    private String matchId;
    private Info info;
    private Team.ParticipantInfo currentSummonerParticipantInfo;
    private Team blue;
    private Team red;


    @Getter
    @Setter
    @NoArgsConstructor
    public static class Info {
        private int gameDuration; // 게임 시간
        private long gameEndStamp; // 끝난 시간
        private int queueType; // 게임 유형 -> 나중에 솔로겜인지, 자유랭인지
        private boolean quickShutdown; // 게임이 빠르게 종료되었는지
        private MaxData maxData;



        @Getter
        @Setter
        @NoArgsConstructor
        public static class MaxData {
            private String defaultRiotGameName;
            private String defaultRiotTag;
            private String defaultChampionName;

            private MaxDamage maxDamage;
            private MaxKill maxKill;
            private MaxDeath maxDeath;
            private MaxAssist maxAssist;

            public void initMaxData() {
                this.maxDamage = new MaxDamage();
                this.maxKill = new MaxKill();
                this.maxDeath = new MaxDeath();
                this.maxAssist = new MaxAssist();
            }
            public void defaultMaxData(String riotGameName, String riotTag, String championName) {
                this.defaultRiotGameName = riotGameName;
                this.defaultRiotTag = riotTag;
                this.defaultChampionName = championName;
            }
            @Getter
            @Setter
            @NoArgsConstructor
            public static class MaxDamage extends MaxStat {
                private int damage;
                private String riotGameName;
                private String riotTag;
                private String championName;

                public void setMaxDamage(int damage, String riotGameName, String riotTag, String championName) {
                    this.damage = damage;
                    this.riotGameName = riotGameName;
                    this.riotTag = riotTag;
                    this.championName = championName;
                }
            }
            @Getter
            @Setter
            @NoArgsConstructor
            public static class MaxKill extends MaxStat {
                private int kill;
                private String riotGameName;
                private String riotTag;
                private String championName;

                public void setMaxKill(int kill, String riotGameName,String riotTag, String championName) {
                    this.kill = kill;
                    this.riotGameName = riotGameName;
                    this.riotTag = riotTag;
                    this.championName = championName;
                }

            }
            @Getter
            @Setter
            @NoArgsConstructor
            public static class MaxDeath extends MaxStat {
                private int death;
                private String riotGameName;
                private String riotTag;
                private String championName;

                public void setMaxDeath(int death, String riotGameName, String riotTag, String championName) {
                    this.death = death;
                    this.riotGameName = riotGameName;
                    this.riotTag = riotTag;
                    this.championName = championName;
                }
            }
            @Getter
            @Setter
            @NoArgsConstructor
            public static class MaxAssist extends MaxStat {
                private int assist;
                private String riotGameName;
                private String riotTag;
                private String championName;

                public void setMaxAssist(int assist, String riotGameName,String riotTag, String championName) {
                    this.assist = assist;
                    this.riotGameName = riotGameName;
                    this.riotTag = riotTag;
                    this.championName = championName;
                }
            }
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Team {
        private String teamName;
        private int totalKills;
        private int totalDeaths;
        private int totalAssists;
        private int totalGold;
        private int teamMaxDamage;
        private Objectives objectives;
        private List<ParticipantInfo> participants = new ArrayList<>();
        private boolean isWin;

        //nullable
//        private Integer grade;
        public void setTeamName(int teamId) {
            switch (teamId) {
                case 100:
                    this.teamName = "Blue";
                    break;
                case 200:
                    this.teamName = "Red";
                    break;
                case 300:
                    this.teamName = "yellow";
                    break;
                case 400:
                    this.teamName = "green";
                    break;
            }
        }

//        public void setObjectives(int baron, int dragon, int horde, int inhibitor, int riftHerald, int tower) {
//            this.objectives = new Objectives();
//            this.objectives.setObjectives(baron, dragon, horde, inhibitor, riftHerald, tower);
//        }
        public void addParticipant(ParticipantInfo participant) {
            participants.add(participant);
        }

        public void addTotalKills(int totalKills) {
            this.totalKills += totalKills;
        }

        public void addTotalDeaths(int totalDeaths) {
            this.totalDeaths += totalDeaths;
        }

        public void addTotalAssists(int totalAssists) {
            this.totalAssists += totalAssists;
        }

        public void addTotalGold(int totalGold) {
            this.totalGold += totalGold;
        }

        public void setMaxDamage(int damage1, int damage2) {
            this.teamMaxDamage = Math.max(damage1, damage2);
        }


        @Getter
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
            public void setObjectives(int baron, int dragon, int horde, int inhibitor, int riftHerald, int tower) {
                this.baron = baron;
                this.dragon = dragon;
                this.horde = horde;
                this.inhibitor = inhibitor;
                this.riftHerald = riftHerald;
                this.tower = tower;
            }
        }

        @Getter
        @Setter
        @NoArgsConstructor
        public static class ParticipantInfo {
            // 라인이 있으면 좋을 듯
            private int teamId; // 팀 아이디
            private boolean isWin;
            private String lane;
            private int kill;
            private boolean isBot;
            private int death;
            private int assist;
            private double killParticipation;
            private double csPerMinute;
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
            private String pUuid;

            public void setLane(String lane) {
                this.lane = lane;
                switch (lane) {
                    case "TOP":
                        this.lane = "TOP";
                        break;
                    case "JUNGLE":
                        this.lane = "JUG";
                        break;
                    case "MIDDLE":
                        this.lane = "MID";
                        break;
                    case "BOTTOM":
                        this.lane = "ADC";
                        break;
                    case "UTILITY":
                        this.lane = "SUP";
                        break;
                }
            }

            @Getter
            @NoArgsConstructor
            public static class Perks {
                private PerkDetail main;
                private PerkDetail sub;

                public void setMain(int mainStyle, List<Integer> mainSelections) {
                    this.main = new PerkDetail(mainStyle, mainSelections);
                }

                public void setSub(int subStyle, List<Integer> subSelections) {
                    this.sub = new PerkDetail(subStyle, subSelections);
                }

                @Getter
                @Setter
                @AllArgsConstructor
                public static class PerkDetail {
                    private int perkStyle;
                    private List<Integer> perkList = new ArrayList<>();
                }

                public void addPerkList(int perkNumber) {
                    main.getPerkList().add(perkNumber);
                }
            }
        }
    }
}