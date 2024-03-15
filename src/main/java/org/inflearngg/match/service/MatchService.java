package org.inflearngg.match.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.inflearngg.client.riot.api.MatchClient;
import org.inflearngg.client.riot.dto.RiotAPIMatchInfo;
import org.inflearngg.match.dto.process.ProcessMatchInfo;
import org.inflearngg.match.dto.process.ProcessRankInfo;
import org.inflearngg.match.mapper.MatchApiMapper;
import org.inflearngg.match.mapper.RankInfoApiMapper;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;


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

    private final int THRED_CNT = 3;

    public String[] getMatchIdsByPuuid(String puuid, int queueType, String region) {
        // Header에 API키 셋팅
        return matchClient.fetchMatchIdListAPI(puuid, queueType , region);

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
        log.debug("[비동기 시작]");
        for (String matchId : matchIdList) {
//            log.info("[matchId]" + matchId);
            CompletableFuture<RiotAPIMatchInfo.MatchBasicInfo> data;
            try {
                data = matchClient.fetchAsyncMatchData(matchId, region);
                if (data == null) {
                    continue;
                }
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
        executorService.shutdown();
        return futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());


    }
}

