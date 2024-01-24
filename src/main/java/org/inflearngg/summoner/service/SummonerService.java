package org.inflearngg.summoner.service;

import lombok.RequiredArgsConstructor;
import org.inflearngg.match.dto.response.MatchingResponseDto;
import org.inflearngg.match.repository.MatchRepository;
import org.inflearngg.summoner.dto.response.SummonerResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SummonerService {

    // 추후 별도 처리 필요
    private String API_KEY = "RGAPI-4368f3ed-e6f2-425f-89a5-2ac000869dbc";
    String API_URL = "https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-puuid/{puuid}?api_key={api_key}";
    String API_URL_RANK = "https://kr.api.riotgames.com/lol/league/v4/entries/by-summoner/{summonerId}?api_key={api_key}";

    // riotAPI 요청으로 소환사 정보 가져오기
    private RestTemplate restTemplate = new RestTemplate();

    //api 요청 횟수 제한

    public SummonerInfo.SummonerBasicInfo getSummonerBasicInfo(String puuid) {
        try {
            ResponseEntity<SummonerInfo.SummonerBasicInfo> summonerEntity = restTemplate.getForEntity(API_URL, SummonerInfo.SummonerBasicInfo.class, puuid, API_KEY);

            if(summonerEntity.getStatusCode().is2xxSuccessful()) {
                return summonerEntity.getBody();
            }
            else {
                throw new RuntimeException("소환사 정보를 가져오는데 실패했습니다.");
            }
        }catch (Exception e) {
            throw new RuntimeException("API 요청 중 오류가 발생 했습니다.", e);
        }
    }
    public SummonerInfo.RankInfo[] getRankData(String SummonerId) {
        try {
            ResponseEntity<SummonerInfo.RankInfo[]> rankDataEntity = restTemplate.getForEntity(API_URL_RANK, SummonerInfo.RankInfo[].class, SummonerId, API_KEY);
            if(rankDataEntity.getStatusCode().is2xxSuccessful()) {
                return rankDataEntity.getBody();
            }
            else {
                throw new RuntimeException("소환사 랭크 정보를 가져오는데 실패했습니다.");
            }
        }catch (Exception e) {
            throw new RuntimeException("API 요청 중 오류가 발생 했습니다.", e);
        }
    }

}
