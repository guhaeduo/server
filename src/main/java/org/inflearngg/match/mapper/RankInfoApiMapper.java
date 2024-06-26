package org.inflearngg.match.mapper;

import org.inflearngg.client.riot.dto.RiotAPIMatchInfo;
import org.inflearngg.match.dto.process.ProcessRankInfo;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.HashMap;

import static org.inflearngg.match.dto.process.ProcessRankInfo.*;

@Component
public class RankInfoApiMapper {
    //client.RiotAPIRankInfo.RankInfo -> dto.process.ProcessRankInfo.RankInfo

    public void setSummonerRankAndLane(RiotAPIMatchInfo.ParticipantInfo participantInfo, ProcessRankInfo rankInfo, HashMap<String, Integer> laneMap) {

        // 개인 렝겜, 혹은 자유 렝겜
//        if (queueType == 420 || queueType == 440) {
            // info 수정
            RankInfo info = rankInfo.getInfo();
            // 이긴 횟수, 진 횟수
            if (participantInfo.isWin()) {
                info.addWin();
            } else {
                info.addLose();
            }

            //총 kill, death, assist, kda
//            double kda = (participantInfo.getKills() + participantInfo.getAssists()) / (double) participantInfo.getDeaths();
            info.addTotalKillsAndTotalDeathsAndTotalAssists(participantInfo.getKills(), participantInfo.getDeaths(), participantInfo.getAssists());

            Lane lane = rankInfo.getLane();
            //ALL
            setRankLane(participantInfo, lane.getAll());
            switch (participantInfo.getTeamPosition()) {
                case "TOP":
                    laneMap.put("TOP", laneMap.getOrDefault("TOP", 0) + 1);
                    setRankLane(participantInfo, lane.getTop());
                    break;
                case "JUNGLE":
                    laneMap.put("JUG", laneMap.getOrDefault("JUG", 0) + 1);
                    setRankLane(participantInfo, lane.getJug());
                    break;
                case "MIDDLE":
                    laneMap.put("MID", laneMap.getOrDefault("MID", 0) + 1);
                    setRankLane(participantInfo, lane.getMid());
                    break;
                case "BOTTOM":
                    laneMap.put("ADC", laneMap.getOrDefault("ADC", 0) + 1);
                    setRankLane(participantInfo, lane.getAdc());
                    break;
                case "UTILITY":
                    laneMap.put("SUP", laneMap.getOrDefault("SUP", 0) + 1);
                    setRankLane(participantInfo, lane.getSup());
                    break;
            }
//        }
    }

    private void setRankLane(RiotAPIMatchInfo.ParticipantInfo participantInfo, RankLaneData laneData) {
        if (participantInfo.isWin()) {
            laneData.addWin();
        } else {
            laneData.addLose();
        }
        if (laneData.isContainChampion(participantInfo.getChampionId())) {
            addChampionData(participantInfo, laneData);
        } else {
            RankLaneData.ChampionData championData = getChampionData(participantInfo);
            laneData.addChampionData(participantInfo.getChampionId(), championData);
        }
    }

    private void addChampionData(RiotAPIMatchInfo.ParticipantInfo participantInfo, RankLaneData lane) {

        RankLaneData.ChampionData championData = lane.getChampions().get(participantInfo.getChampionId());
        championData.addTotalGameCnt();
        if (participantInfo.isWin()) {
            championData.addWin();
        }
        championData.addGameTime(participantInfo.getTimePlayed());
        championData.addTotalCS(participantInfo.getTotalMinionsKilled() + participantInfo.getNeutralMinionsKilled());
        championData.addVisionScore(participantInfo.getVisionScore());
        championData.addKda(participantInfo.getChallenges().getKda());
        championData.addTotalKillParticipation(participantInfo.getChallenges().getKillParticipation());

    }

    @NotNull
    private RankLaneData.ChampionData getChampionData(RiotAPIMatchInfo.ParticipantInfo participantInfo) {
        RankLaneData.ChampionData championData = new RankLaneData.ChampionData(participantInfo.getChampionName(), participantInfo.getChampionId());
        championData.setTotalGameCnt(1);
        if (participantInfo.isWin()) {
            championData.setWins(1);
        }
        championData.setGameTime(participantInfo.getTimePlayed());
        championData.setTotalCS(participantInfo.getTotalMinionsKilled() + participantInfo.getNeutralMinionsKilled());
        championData.setKda(participantInfo.getChallenges().getKda());
        championData.setVisionScore(participantInfo.getVisionScore());
        championData.setTotalKillParticipation(participantInfo.getChallenges().getKillParticipation());
        return championData;
    }
}
