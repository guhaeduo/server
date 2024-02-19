package org.inflearngg.match.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.inflearngg.match.client.MatchClient;
import org.inflearngg.match.client.RiotAPIMatchInfo;
import org.inflearngg.match.dto.process.ProcessMatchInfo;
import org.inflearngg.match.dto.process.ProcessRankInfo;
import org.inflearngg.match.mapper.MatchApiMapper;
import org.inflearngg.match.mapper.RankInfoApiMapper;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;


import java.util.ArrayList;

import java.util.List;
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

    public String[] getMatchIdsByPuuid(String puuid, int queueType) {
        // Header에 API키 셋팅
        return matchClient.fetchMatchIdListAPI(puuid, queueType);

    }

    //SummonerRankInfo
    public ProcessRankInfo getSummonerSummaryInfo(String[] matchIdList, String puuid) throws ExecutionException, InterruptedException {
        ProcessRankInfo processRankInfo = new ProcessRankInfo();
        processRankInfo.initRankInfo();
        calculateSummonerSummaryInfo(matchIdList, puuid, processRankInfo);
        return processRankInfo;
    }


    public void calculateSummonerSummaryInfo(String[] matchIdList, String puuid, ProcessRankInfo rankInfo) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(THRED_CNT);

        log.info("[비동기 시작]");
        for (String matchId : matchIdList) {
            log.info("[matchId]" + matchId);
            CompletableFuture<RiotAPIMatchInfo.MatchBasicInfo> data;
            try {
                data = matchClient.fetchAsyncMatchData(matchId);
                if (data == null) {
                    continue;
                }
                int i = 1;
                for (RiotAPIMatchInfo.ParticipantInfo participantInfo : data.get().getParticipants()) {
                    log.info("match" + matchId + "__" + i);
                    i++;
                    if (participantInfo.getPuuid().equals(puuid)) {
                        rankInfoApiMapper.setSummonerRankAndLane(participantInfo, rankInfo);
                    }
                }
            } catch (HttpClientErrorException e) {
                log.info("찾을 수 없는 게임입니다.");
            } catch (Exception e) {
                log.info("오류가 발생했습니다.");
            }
        }
        executorService.shutdown();

    }


    public List<ProcessMatchInfo> getMatchDataList(String[] matchIdList, String puuid) {
        ArrayList<CompletableFuture<ProcessMatchInfo>> futures = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(THRED_CNT);


        log.info("[비동기 시작]");
        for (String matchId : matchIdList) {
            futures.add(CompletableFuture.supplyAsync(() -> matchClient.fetchAsyncMatchData(matchId))
                    .thenApply(completableFuture -> {
                                RiotAPIMatchInfo.MatchBasicInfo riotApiData = null;
                                try {
                                    riotApiData = completableFuture.get();
                                    //challenges가 null일때가 있음
                                } catch (InterruptedException | ExecutionException e) {
                                    throw new RuntimeException(e);
                                }

                                return matchApiMapper.mapRiotAPIToProcessMatchInfo(riotApiData, puuid);
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

