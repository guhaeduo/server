package org.inflearngg.client.riot.api;

import lombok.RequiredArgsConstructor;
import org.inflearngg.client.riot.dto.RiotApiResponseDto;
import org.inflearngg.summoner.service.SummonerInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
@RequiredArgsConstructor
@Service
public class SummonerClient {


    @Value("${spring.api.key}")
    private String API_KEY;

    private final RestTemplate restTemplate;

    // Header에 API키 셋팅
    HttpHeaders headers = new HttpHeaders();
    HttpEntity<String> entity = new HttpEntity<>(headers);
    String SUMMONER_API_URL;

    public RiotApiResponseDto.RiotSummonerIdInfo fetchSummonerIdAPI(String puuid, String region){
        // Header에 API키 셋팅
        headers.set("X-Riot-Token", API_KEY);
        SUMMONER_API_URL = "https://"+ region + ".api.riotgames.com/lol/summoner/v4/summoners/by-puuid/{puuid}";
        try {
            ResponseEntity<RiotApiResponseDto.RiotSummonerIdInfo> response = restTemplate.exchange(SUMMONER_API_URL, HttpMethod.GET, entity, RiotApiResponseDto.RiotSummonerIdInfo.class, puuid);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            if(response.getStatusCode().is4xxClientError()){
                throw new HttpClientErrorException(response.getStatusCode());
            }else {
                throw new RuntimeException("소환사 정보를 가져오는데 실패했습니다.");
            }
        } catch (Exception e) {
            throw new RuntimeException("API 요청 중 오류가 발생 했습니다.", e);
        }
    }

    public SummonerInfo.RankInfo[] fetchRankData(String region, String SummonerId) throws HttpClientErrorException {
        String API_URL_RANK = "https://" + region + ".api.riotgames.com/lol/league/v4/entries/by-summoner/{summonerId}?api_key={api_key}";
        try {
            ResponseEntity<SummonerInfo.RankInfo[]> rankDataEntity = restTemplate.getForEntity(API_URL_RANK, SummonerInfo.RankInfo[].class, SummonerId, API_KEY);
            if (rankDataEntity.getStatusCode().is2xxSuccessful()) {
                return rankDataEntity.getBody();
            }
            else {
                throw new RuntimeException("소환사 랭크 정보를 가져오는데 실패했습니다.");
            }
        }
        catch (HttpClientErrorException e){
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException("API 요청 중 오류가 발생 했습니다.", e);
        }
    }
}
