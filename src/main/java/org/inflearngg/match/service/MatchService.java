package org.inflearngg.match.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.inflearngg.match.repository.MatchRepository;
import org.inflearngg.match.service.processing.ProcessingData;
import org.inflearngg.match.service.processing.ProcessingMatchInfo;
import org.inflearngg.match.service.processing.ProcessingRankInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.concurrent.ExecutorService;

import static org.inflearngg.match.service.processing.ProcessingMatchInfo.Info.setProcessingTeamAndCurrentSummonerAndMaxDamageData;
import static org.inflearngg.match.service.processing.ProcessingMatchInfo.Team.setObjectiveAndIsWinData;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepository;


    @Value("${spring.api.key}")
    private String API_KEY;
    // puuid, cnt, queue

    //전적갯수
    private final int MATCH_CNT = 10;
    private final int THRED_CNT = 3;
    String API_URL_MathIdList = "https://asia.api.riotgames.com/lol/match/v5/matches/by-puuid/{puuid}/ids?count={cnt}";
    String API_URL_MathData = "https://asia.api.riotgames.com/lol/match/v5/matches/{matchId}";
    private final RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    HttpEntity<String> entity = new HttpEntity<>(headers);
    ResponseEntity<String[]> response = null;


    public String[] getMatchIdsByPuuid(String puuid, int queueType) {
        // Header에 API키 셋팅
        headers.set("X-Riot-Token", API_KEY);
        try {
            if (queueType != 0) {
                API_URL_MathIdList += "&queue={queue}";
                response = restTemplate.exchange(API_URL_MathIdList, HttpMethod.GET, entity, String[].class, puuid, MATCH_CNT, queueType);
            } else
                response = restTemplate.exchange(API_URL_MathIdList, HttpMethod.GET, entity, String[].class, puuid, MATCH_CNT);
            if (response.getStatusCode().is2xxSuccessful()) {

                return response.getBody();
            } else {
                throw new RuntimeException("소환사 정보를 가져오는데 실패했습니다.");
            }
        } catch (Exception e) {
            throw new RuntimeException("API 요청 중 오류가 발생 했습니다.", e);
        }

    }

    public ProcessingData getProcessingData(String[] matchIdList, String puuid) {
        ProcessingData processingData = new ProcessingData();
        // 초기화
        ProcessingRankInfo.SummonerRankInfo.initSummonerRankInfo(processingData);

        // 세팅
        processingData.setMatchDataList(getMatchDataList(matchIdList, puuid, processingData.getSummonerRankInfo()));

        return processingData;
    }

    public List<ProcessingMatchInfo.MatchData> getMatchDataList(String[] matchIdList, String puuid, ProcessingRankInfo.SummonerRankInfo summonerRankInfo) {
        ArrayList<CompletableFuture<ProcessingMatchInfo.MatchData>> futures = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(THRED_CNT);



        log.info("[비동기 시작]");
        for (String matchId : matchIdList) {
            futures.add(CompletableFuture.supplyAsync(() -> getMatchData(matchId))
                    .thenApply(completableFuture -> {
                        RiotAPIMatchInfo.MatchBasicInfo riotApiData = null;
                        try {
                            riotApiData = completableFuture.get();
                            log.info("킬관여울 직접받기 : " + completableFuture.get().getParticipants()[0].getChallenges().getKillParticipation());
                        } catch (InterruptedException | ExecutionException e) {
                            throw new RuntimeException(e);
                        }

                        log.info("킬관여율 : "+riotApiData.getParticipants()[0].getChallenges().getKillParticipation());
                        ProcessingMatchInfo.MatchData processingMatchData = new ProcessingMatchInfo.MatchData();
                        ProcessingMatchInfo.Info.MaxDamage maxDamage = new ProcessingMatchInfo.Info.MaxDamage();
                        ProcessingMatchInfo.Team redTeam = new ProcessingMatchInfo.Team();
                        ProcessingMatchInfo.Team blueTeam = new ProcessingMatchInfo.Team();
                        setProcessingTeamAndCurrentSummonerAndMaxDamageData(riotApiData.getQueueId() ,riotApiData.getParticipants(), maxDamage, redTeam, blueTeam, processingMatchData, summonerRankInfo , puuid);
                        // 오브젝트 계산, 승리여부도 추가
                        setObjectiveAndIsWinData(riotApiData.getTeams(), redTeam, blueTeam);
                        ProcessingMatchInfo.setProcessingInfoData(riotApiData, maxDamage, processingMatchData);
                        processingMatchData.setRed(redTeam);
                        processingMatchData.setBlue(blueTeam);
                        return processingMatchData;
                    }));
        }
        executorService.shutdown();
        return futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

    }



    @Async
    public CompletableFuture<RiotAPIMatchInfo.MatchBasicInfo> getMatchData(String matchId) {

        headers.set("X-Riot-Token", API_KEY);
        try {
            ResponseEntity<RiotAPIMatchInfo.ApiData> matchInfo = restTemplate.exchange(API_URL_MathData, HttpMethod.GET, entity, RiotAPIMatchInfo.ApiData.class, matchId);
            if (matchInfo.getStatusCode().is2xxSuccessful()) {
//                내용전부 출력해보기
//                String threadName = Thread.currentThread().getName();
//                log.info("Thread name: " + threadName);
                return CompletableFuture.completedFuture(matchInfo.getBody().getInfo());
            } else {
                throw new RuntimeException("소환사 정보를 가져오는데 실패했습니다.");
            }
        } catch (Exception e) {
            throw new RuntimeException("API 요청 중 오류가 발생 했습니다.", e);
        }
    }

}
