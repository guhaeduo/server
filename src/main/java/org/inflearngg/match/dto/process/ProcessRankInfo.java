package org.inflearngg.match.dto.process;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
@NoArgsConstructor
public class ProcessRankInfo {
    private RankInfo info;
    private Lane lane;

    public void initRankInfo() {
        this.info = new RankInfo();
        this.lane = new Lane();

    }


    @Getter
    @NoArgsConstructor
    public static class RankInfo {
        private int cntGame;
//        private String winningRate;
        private int wins;
        private int loses;
        private double sumKda;
        private int totalKills;
        private int totalDeaths;
        private int totalAssists;


        public void addGameCnt() {
            this.cntGame += 1;
        }
        public void isWin() {
            this.wins += 1;
            this.cntGame += 1;
        }

        public void isLose() {
            this.loses += 1;
            this.cntGame += 1;
        }

        public void addKdaAndTotalKillsAndTotalDeathsAndTotalAssists(int totalKills, int totalDeaths, int totalAssists, double kda) {
            this.totalKills += totalKills;
            this.totalDeaths += totalDeaths;
            this.totalAssists += totalAssists;
            this.sumKda += kda;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Lane {
        private RankLaneData all;
        private RankLaneData top;
        private RankLaneData jug;
        private RankLaneData mid;
        private RankLaneData adc;
        private RankLaneData sup;


        public void initLane() {
            this.all = new RankLaneData();
            this.top = new RankLaneData();
            this.jug = new RankLaneData();
            this.mid = new RankLaneData();
            this.adc = new RankLaneData();
            this.sup = new RankLaneData();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class RankLaneData {
        private int totalGameCnt;
        private int wins;
        private int loses;
        private HashMap<Integer, ChampionData> champions = new HashMap<>();

        public void addWin() {
            this.wins += 1;
            this.totalGameCnt += 1;
        }

        public void addLose() {
            this.loses += 1;
            this.totalGameCnt += 1;

        }
        public boolean isContainChampion(int championIconNumber) {
            return champions.containsKey(championIconNumber);
        }

        public void addChampionData(int championIconNumber, ChampionData championData) {
            champions.put(championIconNumber, championData);
        }

        public ChampionData getChampionData(int championIconNumber) {
            return champions.get(championIconNumber);
        }

        @Getter
        @Setter
        @NoArgsConstructor
        public static class ChampionData {
            private String championName;
            private int championIconNumber;
            private int totalGameCnt;
            private int wins;
            private int gameTime;
            private int totalCS;
            private double visionScorePerMinute;
            private double kda;
            private double totalKillParticipation;

            public ChampionData(String championName, int championIconNumber) {
                this.championName = championName;
                this.championIconNumber = championIconNumber;
            }
            public void addTotalGameCnt() {
                this.totalGameCnt += 1;
            }

            public void addWin() {
                this.wins += 1;
            }

            public void addGameTime(int gameTime) {
                this.gameTime += gameTime;
            }
            public void addTotalCS(int totalCS) {
                this.totalCS += totalCS;
            }
            public void addVisionScorePerMinute(double visionScorePerMinute) {
                this.visionScorePerMinute += visionScorePerMinute;
            }
            public void addKda(double kda) {
                this.kda += kda;
            }
            public void addTotalKillParticipation(double totalKillParticipation) {
                this.totalKillParticipation += totalKillParticipation;
            }
        }
    }
}

