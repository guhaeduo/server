package org.inflearngg.match.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.inflearngg.client.riot.api.MatchClient;
import org.inflearngg.client.riot.dto.RiotAPIMatchInfo;
import org.inflearngg.client.riot.exception.NotFoundRiotClientErrorException;
import org.inflearngg.match.dto.process.ProcessMatchInfo;
import org.inflearngg.match.dto.process.ProcessRankInfo;
import org.inflearngg.match.mapper.MatchApiMapper;
import org.inflearngg.match.mapper.RankInfoApiMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;


import java.time.LocalDateTime;
import java.util.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.concurrent.ExecutorService;


@Slf4j
@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchClient matchClient;
    private final RankInfoApiMapper rankInfoApiMapper;
    private final MatchApiMapper matchApiMapper;

    private final int THRED_CNT = 5;

    public String[] getMatchIdsByPuuid(String puuid, int queueType, String region) {
        // Header에 API키 셋팅
        String[] strings = matchClient.fetchMatchIdListAPI(puuid, queueType, region);
        if (strings.length == 0)
            throw new NotFoundRiotClientErrorException(HttpStatus.NOT_FOUND, "소환사의 게임 정보를 찾을 수 없습니다.");
        return strings;

    }

    //SummonerRankInfo
    public ProcessRankInfo getSummonerSummaryInfo(String[] matchIdList, String puuid,String region) throws ExecutionException, InterruptedException {
        ProcessRankInfo processRankInfo = new ProcessRankInfo();
        processRankInfo.initRankInfo();
        calculateSummonerSummaryInfo(matchIdList, puuid, processRankInfo, region);
        return processRankInfo;
    }


    public void calculateSummonerSummaryInfo(String[] matchIdList, String puuid, ProcessRankInfo rankInfo, String region) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(THRED_CNT);
        HashMap<String, Integer> laneMap = new HashMap<>();
        log.info("[비동기 시작]" + LocalDateTime.now());
        for (String matchId : matchIdList) {
            CompletableFuture<RiotAPIMatchInfo.MatchBasicInfo> data;
            try {
                data = matchClient.fetchAsyncMatchData(matchId, region);
                if (data == null) {
                    continue;
                }
                log.info("비동기중" +  matchId + " : " + LocalDateTime.now());
                for (RiotAPIMatchInfo.ParticipantInfo participantInfo : data.get().getParticipants()) {
                    if (participantInfo.getPuuid().equals(puuid)) {
                        rankInfoApiMapper.setSummonerRankAndLane(participantInfo, rankInfo, laneMap);
                    }
                }
            } catch (HttpClientErrorException e) {
                log.info("찾을 수 없는 게임입니다.");
            } catch (Exception e) {
                log.info("오류가 발생했습니다.");
            }
        }
        //laneMap value 값 내림차순 정렬
        // 현재시간 출력
        log.info("비동기중 현재 시간 : " +  LocalDateTime.now());
        setMainLaneAndSubLaneSummary(rankInfo, laneMap);
        executorService.shutdown();

    }

    private static void setMainLaneAndSubLaneSummary(ProcessRankInfo rankInfo, HashMap<String, Integer> laneMap) {
        PriorityQueue<HashMap.Entry<String, Integer>> pq = new PriorityQueue<>((a, b) -> b.getValue() - a.getValue());
        pq.addAll(laneMap.entrySet());
        if(!pq.isEmpty()){
            String mainLane = pq.poll().getKey();
            rankInfo.getInfo().setMainLane(mainLane);
        }
        if(!pq.isEmpty()){
            String subLane = pq.poll().getKey();
            rankInfo.getInfo().setSubLane(subLane);
        }
    }


    public List<ProcessMatchInfo> getMatchDataList(String[] matchIdList, String puuid, String region) {
        ArrayList<CompletableFuture<ProcessMatchInfo>> futures = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(THRED_CNT);
        log.debug("[비동기 시작]");
        for (String matchId : matchIdList) {
            futures.add(CompletableFuture.supplyAsync(() -> matchClient.fetchAsyncMatchData(matchId,region))
                    .thenApply(completableFuture -> {
                                RiotAPIMatchInfo.MatchBasicInfo riotApiData = null;
                                try {
                                    if (completableFuture == null) {
                                        return null;
                                    }
                                    riotApiData = completableFuture.get();
                                    //challenges가 null일때가 있음
                                } catch (InterruptedException | ExecutionException e) {
                                    throw new RuntimeException(e);
                                }
                                return matchApiMapper.mapRiotAPIToProcessMatchInfo(riotApiData, puuid, matchId);
                            }
                    )
            );
        }
        log.debug( "비동기중 현재 시간 : " + System.currentTimeMillis() );
        executorService.shutdown();

        List<ProcessMatchInfo> collect = futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
        if (collect.isEmpty())
            throw new NotFoundRiotClientErrorException(HttpStatus.NOT_FOUND, "소환사의 게임 정보를 찾을 수 없습니다.");
        return collect;
    }
}

