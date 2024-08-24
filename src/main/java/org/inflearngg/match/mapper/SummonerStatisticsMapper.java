package org.inflearngg.match.mapper;

import org.inflearngg.client.riot.domain.ConcurrentDataHolder;
import org.inflearngg.client.riot.dto.RiotApiMatchInfo;
import org.inflearngg.match.dto.process.ProcessRankInfo;

import java.util.concurrent.ConcurrentHashMap;

public class SummonerStatisticsMapper {

    public void test1(RiotApiMatchInfo.ParticipantInfo participantInfo, ConcurrentDataHolder totalDataHolder, ConcurrentHashMap<String, ConcurrentDataHolder> laneMap, ConcurrentHashMap<String, ConcurrentDataHolder> champMap) {


        //전체 게임 정보 수정
        //라인별 게임 정보 수정
        //챔프별 게임 정보 수정
    }


        //전체 게임 정보 수정
    private void sumTotalInfo(RiotApiMatchInfo.ParticipantInfo participantInfo, ConcurrentDataHolder total) {
        // 전체게임수
        //전체 승리수
        //전체 패배수
        // 전체 kda 합
        // 전체 킬수
        // 전체 데스 수
        // 전체 어시스트 수
        // 전체 cs수


    }
        //라인별 게임 정보 수정
    private void sumLaneInfo(RiotApiMatchInfo.ParticipantInfo participantInfo, ConcurrentDataHolder lane) {
        //라인별 게임수
        //라인별 승리수
    }

        //챔프별 게임 정보 수정
    private void sumChampInfo(RiotApiMatchInfo.ParticipantInfo participantInfo, ConcurrentDataHolder champ) {
        //챔프별 게임수
        //챔프별 승리수
        //챔프별 패배수
        //챔프별 게임시간
        //챔프별 cs
        //챔프별 킬수
        //챔프별 데스수
        //챔프별 어시스트수
        //챔프별 와드점수
        //챔프별 kda
        //챔프별 킬관여율
    }
}