package org.inflearngg.match.service;

import lombok.RequiredArgsConstructor;
import org.inflearngg.match.dto.response.MatchingResponseDto;
import org.inflearngg.match.repository.MatchRepository;
import org.inflearngg.user.service.RiotApiService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepository;

    // 추후 별도 처리 필요
    private String API_KEY = "RGAPI-4368f3ed-e6f2-425f-89a5-2ac000869dbc";
    // puuid, cnt, queue
    String API_URL_MathIdList = "https://asia.api.riotgames.com/lol/match/v5/matches/by-puuid/{puuid}/ids?count={cnt}";

    private RestTemplate restTemplate = new RestTemplate();



    public String[] getMatchIdsByPuuid(String puuid, int queueType) {
        //Header Param 으 "X-Riot-Token": API_KEY
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Riot-Token", API_KEY);
        // HttpEntity에 헤더 추가
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String[]> response = null;

        try {
            // 출력해보기
            System.out.println("queue : " + queueType);
            if (queueType != 0) {
                API_URL_MathIdList += "&queue={queue}";
                response = restTemplate.exchange(API_URL_MathIdList, HttpMethod.GET, entity, String[].class, puuid, 60, queueType);
            } else
                response = restTemplate.exchange(API_URL_MathIdList, HttpMethod.GET, entity, String[].class, puuid, 60);
            if (response.getStatusCode().is2xxSuccessful()) {
                // 리스트 출력해보기
                int i = 1;
                for (String matchId : response.getBody()) {
                    System.out.println( i + "번째: matchId = " + matchId);
                    i++;
                }

                return response.getBody();
            } else {
                throw new RuntimeException("소환사 정보를 가져오는데 실패했습니다.");
            }
        }catch (Exception e) {
            throw new RuntimeException("API 요청 중 오류가 발생 했습니다.", e);
        }

    }

    public List<MatchingResponseDto.MatchData> getMatchDataListByPuuid(int puuid) {
        return null; // 임시 코드

    }
}
