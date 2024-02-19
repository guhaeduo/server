package org.inflearngg.summoner.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class SummonerService {

    @Value("${spring.api.key}")
    private String API_KEY;

    // kr br, na, euw, eune, jp, oc, las, lan, ru, tr 등 앞에 링크 만 변경하면됨.

    String API_URL = "https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-puuid/{puuid}?api_key={api_key}";
    // 리그 정보 가져오기
//    String API_URL_RANK = ".api.riotgames.com/lol/league/v4/entries/by-summoner/{summonerId}?api_key={api_key}";

    // riotAPI 요청으로 소환사 정보 가져오기
    private final RestTemplate restTemplate = new RestTemplate();

    //api 요청 횟수 제한

    public SummonerInfo.SummonerBasicInfo getSummonerBasicInfo(String puuid) {
        try {
            ResponseEntity<SummonerInfo.SummonerBasicInfo> summonerEntity = restTemplate.getForEntity(API_URL, SummonerInfo.SummonerBasicInfo.class, puuid, API_KEY);

            if (summonerEntity.getStatusCode().is2xxSuccessful()) {
                return summonerEntity.getBody();
            } else {
                throw new RuntimeException("소환사 정보를 가져오는데 실패했습니다.");
            }
        } catch (Exception e) {
            throw new RuntimeException("API 요청 중 오류가 발생 했습니다.", e);
        }
    }

    public SummonerInfo.RankInfo[] getRankData(String region, String SummonerId) throws HttpClientErrorException {
        String API_URL_RANK = "https://" + region + ".api.riotgames.com/lol/league/v4/entries/by-summoner/{summonerId}?api_key={api_key}";
        log.info("API_URL_RANK : " + API_URL_RANK);
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
