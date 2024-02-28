package org.inflearngg.summoner.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.inflearngg.aop.dto.Region;
import org.inflearngg.summoner.client.AccountClient;
import org.inflearngg.summoner.client.RiotApiResponseDto;
import org.inflearngg.summoner.client.SummonerClient;
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

    private final RestTemplate restTemplate;
    private final AccountClient accountClient;
    private final SummonerClient summonerClient;


    public RiotApiResponseDto.RiotSummonerIdInfo checkAndGetIdList(String gameName, String gameTag, Region region){
        RiotApiResponseDto.RiotPuuid puuid = getPuuid(gameName, gameTag, region.getContinent());
        return getSummonerIdInfo(puuid.getPuuid(), region.name());

    }


    public  RiotApiResponseDto.RiotPuuid getPuuid(String gameName, String gameTag, String continent) {
         return accountClient.fetchPuuidAPI(gameName, gameTag, continent);
    }

    public RiotApiResponseDto.RiotSummonerIdInfo getSummonerIdInfo(String puuid, String region){
        return summonerClient.fetchSummonerIdAPI(puuid, region);
    }



    public SummonerInfo.RankInfo[] getRankData(String region, String SummonerId) throws HttpClientErrorException {
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
