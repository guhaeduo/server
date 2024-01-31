package org.inflearngg.match.service.processing;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.inflearngg.match.service.MatchInfo;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;


@Slf4j
@Getter
@Setter
@NoArgsConstructor
public class ProcessingRankInfo {
    @Getter
    @Setter
    @NoArgsConstructor
    public static class SummonerRankInfo {
        private RankInfo info;
        private Lane lane;

        public static void initSummonerRankInfo(ProcessingData processingData) {
            SummonerRankInfo summonerRankInfo = new SummonerRankInfo();

            // 랭크
            summonerRankInfo.setInfo(new ProcessingRankInfo.SummonerRankInfo.RankInfo());
            //라인
            summonerRankInfo.setLane(new ProcessingRankInfo.SummonerRankInfo.Lane());

            //라인별 챔피언
            summonerRankInfo.getLane().setAll(new ProcessingRankInfo.SummonerRankInfo.Lane.RankLaneData());
            summonerRankInfo.getLane().setTop(new ProcessingRankInfo.SummonerRankInfo.Lane.RankLaneData());
            summonerRankInfo.getLane().setJug(new ProcessingRankInfo.SummonerRankInfo.Lane.RankLaneData());
            summonerRankInfo.getLane().setMid(new ProcessingRankInfo.SummonerRankInfo.Lane.RankLaneData());
            summonerRankInfo.getLane().setAdc(new ProcessingRankInfo.SummonerRankInfo.Lane.RankLaneData());
            summonerRankInfo.getLane().setSup(new ProcessingRankInfo.SummonerRankInfo.Lane.RankLaneData());


            //Setting
            processingData.setSummonerRankInfo(summonerRankInfo);

        }

        public static void setSummonerRankAndLane(MatchInfo.ParticipantInfo participantInfo, SummonerRankInfo summonerRankInfo, int queueType) {
            log.info("queue 타입 : " + queueType);

            // 개인 렝겜, 혹은 자유 렝겜
            if (queueType == 420 || queueType == 440) {
                // info 수정
                ProcessingRankInfo.SummonerRankInfo.RankInfo rankInfo = summonerRankInfo.getInfo();
                rankInfo.setCntGame(rankInfo.getCntGame() + 1);
                if (participantInfo.isWin()) {
                    rankInfo.setWins(rankInfo.getWins() + 1);
                } else {
                    rankInfo.setLoses(rankInfo.getLoses() + 1);
                }
                //총 kill, death, assist, kda
                rankInfo.setTotalKills(rankInfo.getTotalKills() + participantInfo.getKills());
                rankInfo.setTotalDeaths(rankInfo.getTotalDeaths() + participantInfo.getDeaths());
                rankInfo.setTotalAssists(rankInfo.getTotalAssists() + participantInfo.getAssists());
                rankInfo.setKda(rankInfo.getKda() + participantInfo.getChallenges().getKda());

                // RankLaneData lane 별 수정
                // 매판 해당 게임에서 진행한 라인, 챔피언, CS, 시야점수, KDA, 킬관여율
                Lane lane = summonerRankInfo.getLane();
                setLaneData(participantInfo, lane.getAll());
                if (participantInfo.getTeamPosition().equals("TOP")) {
                    setLaneData(participantInfo, lane.getTop());
                }
                if (participantInfo.getTeamPosition().equals("JUNGLE")) {
                    setLaneData(participantInfo, lane.getJug());
                }
                if (participantInfo.getTeamPosition().equals("MIDDLE")) {
                    setLaneData(participantInfo, lane.getMid());
                }
                if (participantInfo.getTeamPosition().equals("BOTTOM")) {
                    setLaneData(participantInfo, lane.getAdc());
                }
                if (participantInfo.getTeamPosition().equals("UTILITY")) {
                    setLaneData(participantInfo, lane.getSup());
                }

            }
        }

        private static void setLaneData(MatchInfo.ParticipantInfo participantInfo, Lane.RankLaneData rankLaneData) {

            // 전체게임
            rankLaneData.setTotalGameCnt(rankLaneData.getTotalGameCnt() + 1);
            // 이긴 횟수
            if (participantInfo.isWin()) {
                rankLaneData.setWins(rankLaneData.getWins() + 1);
            }

            if (rankLaneData.getChampions() == null) {
                rankLaneData.setChampions(new HashMap<>());
            }
            // 만약 hashmap에 챔피언이 없다면 추가
            if (!rankLaneData.getChampions().containsKey(participantInfo.getChampionId())) {
                Lane.RankLaneData.ChampionData championData = getChampionData(participantInfo);
                rankLaneData.getChampions().put(participantInfo.getChampionId(), championData);
            } else {
                addChampionData(participantInfo, rankLaneData);
            }
        }

        private static void addChampionData(MatchInfo.ParticipantInfo participantInfo, Lane.RankLaneData lane) {
            Lane.RankLaneData.ChampionData championData = lane.getChampions().get(participantInfo.getChampionId());
            championData.setTotalGameCnt(championData.getTotalGameCnt() + 1);
            if (participantInfo.isWin()) {
                championData.setWins(championData.getWins() + 1);
            }
            championData.setGameTime(championData.getGameTime() + participantInfo.getTimePlayed());
            championData.setTotalCS(championData.getTotalCS() + participantInfo.getTotalMinionsKilled());
            championData.setVisionScorePerMinute(championData.getVisionScorePerMinute() + participantInfo.getChallenges().getVisionScorePerMinute());
            championData.setKda((championData.getKda() + participantInfo.getChallenges().getKda()));
            championData.setTotalKillParticipation(championData.getTotalKillParticipation() + participantInfo.getChallenges().getKillParticipation());
        }

        @NotNull
        private static Lane.RankLaneData.ChampionData getChampionData(MatchInfo.ParticipantInfo participantInfo) {
            Lane.RankLaneData.ChampionData championData = new Lane.RankLaneData.ChampionData();
            championData.setChampionName(participantInfo.getChampionName());
            championData.setChampionIconNumber(participantInfo.getChampionId());
            championData.setTotalGameCnt(1);
            championData.setWins(1);
            championData.setGameTime(participantInfo.getTimePlayed());
            championData.setTotalCS(participantInfo.getTotalMinionsKilled());
            championData.setKda(participantInfo.getChallenges().getKda());
            championData.setVisionScorePerMinute(participantInfo.getChallenges().getVisionScorePerMinute());
            championData.setTotalKillParticipation(participantInfo.getChallenges().getKillParticipation());
            return championData;
        }


        @Getter
        @Setter
        @NoArgsConstructor
        public static class RankInfo {
            private int cntGame;
            private String winningRate;
            private int wins;
            private int loses;
            private double kda;
            private int totalKills;
            private int totalDeaths;
            private int totalAssists;

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

            @Getter
            @Setter
            @NoArgsConstructor
            public static class RankLaneData {
                private int totalGameCnt;
                private int wins;
                //라인별 챔피언
                private HashMap<Integer, ChampionData> champions;
                //                private <ChampionData> champions;


                // 한게임에 들어갈 데이터
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
                }

                @Getter
                @Setter
                @NoArgsConstructor
                public static class RankLaneChampionData {
                    private String championName;
                    private int championIconNumber;
                    private String winningRate;
                    private String csAvg;
                    private int visionScore;
                    private String kda;
                    private String killParticipation;
                }
            }
        }
    }
}
