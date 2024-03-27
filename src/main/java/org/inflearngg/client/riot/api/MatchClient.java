package org.inflearngg.client.riot.api;

import lombok.extern.slf4j.Slf4j;
import org.inflearngg.client.riot.dto.RiotAPIMatchInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class MatchClient {

    @Value("${spring.api.key}")
    private String API_KEY;


    private final int MATCH_CNT = 20;
    String region = "asia";
    String API_URL_MathIdList = "https://"+ region + ".api.riotgames.com/lol/match/v5/matches/by-puuid/{puuid}/ids?count={cnt}";
    String API_URL_MathInfo = "https://"+ region +".api.riotgames.com/lol/match/v5/matches/{matchId}";
    private final RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    HttpEntity<String> entity = new HttpEntity<>(headers);
    ResponseEntity<String[]> response = null;

    public String[] fetchMatchIdListAPI(String puuid, int queueType, String region) {
        // Header에 API키 셋팅
        headers.set("X-Riot-Token", API_KEY);

        API_URL_MathIdList = "https://"+ region + ".api.riotgames.com/lol/match/v5/matches/by-puuid/{puuid}/ids?count={cnt}";
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
        }catch (Exception e) {
            throw new RuntimeException("API 요청 중 오류가 발생 했습니다.", e);
        }
    }

    @Async
    public CompletableFuture<RiotAPIMatchInfo.MatchBasicInfo> fetchAsyncMatchData(String matchId, String region) {
        ResponseEntity<RiotAPIMatchInfo.ApiData> matchInfo;

        API_URL_MathInfo = "https://"+ region +".api.riotgames.com/lol/match/v5/matches/{matchId}";
        headers.set("X-Riot-Token", API_KEY);
        try {
            matchInfo = restTemplate.exchange(API_URL_MathInfo, HttpMethod.GET, entity, RiotAPIMatchInfo.ApiData.class, matchId);
            if (matchInfo.getStatusCode().is2xxSuccessful()) {
                return CompletableFuture.completedFuture(matchInfo.getBody().getInfo());
            }
            if (matchInfo.getStatusCode().is4xxClientError()) {
                throw new HttpClientErrorException(matchInfo.getStatusCode());
            } else {
                throw new RuntimeException();
            }
        } catch (HttpClientErrorException e) {
            log.info("matchId : " + matchId + "에 대한 정보를 가져오는데 실패했습니다.");
            return null;
        } catch (Exception e) {
            throw new RuntimeException("API 요청 중 예기치못한 오류가 발생 했습니다.", e);
        }
    }
}

