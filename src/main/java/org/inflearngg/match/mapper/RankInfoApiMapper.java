package org.inflearngg.match.mapper;

import org.inflearngg.match.client.RiotAPIMatchInfo;
import org.inflearngg.match.dto.process.ProcessRankInfo;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.HashMap;

import static org.inflearngg.match.dto.process.ProcessRankInfo.*;

@Component
public class RankInfoApiMapper {
    //client.RiotAPIRankInfo.RankInfo -> dto.process.ProcessRankInfo.RankInfo

    public void setSummonerRankAndLane(RiotAPIMatchInfo.ParticipantInfo participantInfo, ProcessRankInfo rankInfo) {

        // 개인 렝겜, 혹은 자유 렝겜
//        if (queueType == 420 || queueType == 440) {
            // info 수정
            RankInfo info = rankInfo.getInfo();
            // 이긴 횟수, 진 횟수
            if (participantInfo.isWin()) {
                info.isWin();
            } else {
                info.isLose();
            }

            //총 kill, death, assist, kda
            info.addKdaAndTotalKillsAndTotalDeathsAndTotalAssists(participantInfo.getKills(), participantInfo.getDeaths(), participantInfo.getAssists(), participantInfo.getChallenges().getKda());

            Lane lane = rankInfo.getLane();
            lane.initLane();
            //ALL
            setRankLane(participantInfo, lane.getAll());
            switch (participantInfo.getTeamPosition()) {
                case "TOP":
                    setRankLane(participantInfo, lane.getTop());
                    break;
                case "JUNGLE":
                    setRankLane(participantInfo, lane.getJug());
                    break;
                case "MIDDLE":
                    setRankLane(participantInfo, lane.getMid());
                    break;
                case "BOTTOM":
                    setRankLane(participantInfo, lane.getAdc());
                    break;
                case "UTILITY":
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
        championData.setTotalGameCnt(championData.getTotalGameCnt() + 1);
        championData.addTotalGameCnt();
        if (participantInfo.isWin()) {
            championData.addWin();
        }
        championData.addGameTime(participantInfo.getTimePlayed());
        championData.addTotalCS(participantInfo.getTotalMinionsKilled());
        championData.addVisionScorePerMinute(participantInfo.getChallenges().getVisionScorePerMinute());
        championData.addKda(participantInfo.getChallenges().getKda());
        championData.addTotalKillParticipation(participantInfo.getChallenges().getKillParticipation());

    }

    @NotNull
    private RankLaneData.ChampionData getChampionData(RiotAPIMatchInfo.ParticipantInfo participantInfo) {
        RankLaneData.ChampionData championData = new RankLaneData.ChampionData(participantInfo.getChampionName(), participantInfo.getChampionId());
        championData.setTotalGameCnt(1);
        championData.setWins(1);
        championData.setGameTime(participantInfo.getTimePlayed());
        championData.setTotalCS(participantInfo.getTotalMinionsKilled());
        championData.setKda(participantInfo.getChallenges().getKda());
        championData.setVisionScorePerMinute(participantInfo.getChallenges().getVisionScorePerMinute());
        championData.setTotalKillParticipation(participantInfo.getChallenges().getKillParticipation());
        return championData;
    }
}
