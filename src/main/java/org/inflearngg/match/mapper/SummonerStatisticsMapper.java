package org.inflearngg.match.mapper;

import org.inflearngg.client.riot.domain.ConcurrentChampDataHolder;
import org.inflearngg.client.riot.domain.ConcurrentTotalDataHolder;
import org.inflearngg.client.riot.domain.ConcurrentLaneDataHolder;
import org.inflearngg.client.riot.dto.RiotApiMatchInfo;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class SummonerStatisticsMapper {

    int quickShutdown = 300;

    public void SummonerInfoUpdate(RiotApiMatchInfo.ParticipantInfo participantInfo,
                                   ConcurrentTotalDataHolder totalDataHolder,
                                   ConcurrentHashMap<String, ConcurrentLaneDataHolder> laneMap,
                                   ConcurrentHashMap<Integer, ConcurrentChampDataHolder> champMap) {

        if (participantInfo.getTimePlayed() < quickShutdown) { // 다시하기 제외
            return;
        }
        //전체 게임 정보 수정
        sumTotalInfo(participantInfo, totalDataHolder);
        //라인별 게임 정보 수정
        // ALL은 무조건 추가
        laneMap.put("ALL", laneMap.getOrDefault("ALL", new ConcurrentLaneDataHolder()));

        sumLaneInfo(participantInfo, laneMap);
        //챔프별 게임 정보 수정
        sumChampInfo(participantInfo, champMap);
    }


    //전체 게임 정보 수정
    private void sumTotalInfo(RiotApiMatchInfo.ParticipantInfo info, ConcurrentTotalDataHolder total) {
        // 전체게임수
        total.sumTotalGameCnt();
        if (info.isWin()) {
            //전체 승리수
            total.sumTotalWinCnt();
        } else {
            //전체 패배수
            total.sumTotalLoseCnt();
        }
        double kda = (info.getKills() + info.getAssists()) / (double) info.getDeaths();
        // 전체 kda 합
        total.sumTotalKda(kda);
        // 전체 킬수
        total.sumTotalKillCnt(info.getKills());
        // 전체 데스 수
        total.sumTotalDeathCnt(info.getDeaths());
        // 전체 어시스트 수
        total.sumTotalAssistCnt(info.getAssists());
        // 전체 cs수
        total.sumTotalCsCnt(info.getTotalMinionsKilled());
    }

    //라인별 게임 정보 수정
    private void sumLaneInfo(RiotApiMatchInfo.ParticipantInfo info, ConcurrentHashMap<String, ConcurrentLaneDataHolder> laneMap) {
        String lane = changeLaneName(info.getTeamPosition());

        // "ALL" 라인의 데이터 처리
        laneMap.computeIfAbsent("ALL", key -> new ConcurrentLaneDataHolder()).sumLaneGameCnt();

        // 특정 라인의 데이터 처리
        ConcurrentLaneDataHolder laneData = laneMap.computeIfAbsent(lane, key -> new ConcurrentLaneDataHolder());
        laneData.sumLaneGameCnt();

        if (info.isWin()) {
            //라인별 승리수
            laneData.sumLaneWinCnt();
            laneMap.get("ALL").sumLaneWinCnt();
        }
    }

    /**
     * 챔프별 게임 정보 수정
     * 챔프네임 -> id 로 변경
     */
    //
    private void sumChampInfo(RiotApiMatchInfo.ParticipantInfo info, ConcurrentHashMap<Integer, ConcurrentChampDataHolder> champMap) {
        //String champ = info.getChampionName();
        int champ = info.getChampionId();
        ConcurrentChampDataHolder champData = champMap.computeIfAbsent(champ, key -> new ConcurrentChampDataHolder());
        //챔프별 게임수
        champData.sumChampGameCnt();
        //챔프별 승리수
        if (info.isWin()) {
            champData.sumChampWinCnt();
        }
        //챔프별 게임시간
        champData.sumChampGameDuring(info.getTimePlayed());
        //챔프별 cs
        champData.sumChampCsCnt(info.getTotalMinionsKilled());
        //챔프별 킬수
        champData.sumChampKillCnt(info.getKills());
        //챔프별 데스수
        champData.sumChampDeathCnt(info.getDeaths());
        //챔프별 어시스트수
        champData.sumChampAssistCnt(info.getAssists());
        //챔프별 와드점수
        champData.sumChampVisionScore(info.getVisionScore());
        //챔프별 kda
        double kda = (info.getKills() + info.getAssists()) / (double) info.getDeaths();
        champData.sumChampKda(kda);
        //챔프별 킬관여율
        double killParticipation = (info.getKills() + info.getAssists()) / (double) champData.getChampKillCnt().get();
        champData.sumChampKillParticipation(killParticipation);
    }

    /**
     * 라인별 정보 수정
     * TOP -> TOP, JUNGLE -> JUG, MIDDLE -> MID, BOTTOM -> ADC, UTILITY -> SUP
     */
    private String changeLaneName(String lane) {
        switch (lane) {
            case "TOP":
                return "TOP";
            case "JUNGLE":
                return "JUG";
            case "MIDDLE":
                return "MID";
            case "BOTTOM":
                return "ADC";
            case "UTILITY":
                return "SUP";
            default:
                return "ALL";
        }
    }

}