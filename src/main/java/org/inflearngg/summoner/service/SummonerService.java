package org.inflearngg.summoner.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.inflearngg.aop.dto.Region;
import org.inflearngg.client.riot.api.AccountClient;
import org.inflearngg.client.riot.dto.RiotApiResponseDto;
import org.inflearngg.client.riot.api.SummonerClient;
import org.inflearngg.duo.dto.QueueType;
import org.springframework.beans.factory.annotation.Value;
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


    public RiotApiResponseDto.RiotSummonerInfo checkAndGetIdList(String gameName, String gameTag, Region region) {
        RiotApiResponseDto.RiotSummonerInfo riotSummonerInfo = new RiotApiResponseDto.RiotSummonerInfo();
        RiotApiResponseDto.RiotPuuid puuid = getPuuid(gameName, gameTag);
        RiotApiResponseDto.RiotSummonerIdInfo summonerIdInfo = getSummonerIdInfo(puuid.getPuuid(), region.name());
        riotSummonerInfo.setGameName(puuid.getGameName());
        riotSummonerInfo.setTagLine(puuid.getTagLine());
        riotSummonerInfo.setPuuid(puuid.getPuuid());

        riotSummonerInfo.setAccountId(summonerIdInfo.getAccountId());
        riotSummonerInfo.setSummonerId(summonerIdInfo.getId());
        riotSummonerInfo.setProfileIconId(summonerIdInfo.getProfileIconId());
        riotSummonerInfo.setRevisionDate(summonerIdInfo.getRevisionDate());
        riotSummonerInfo.setSummonerLevel(summonerIdInfo.getSummonerLevel());
        return riotSummonerInfo;
    }

    public RiotApiResponseDto.RiotPuuidAndTierInfo checkAndGetTier(String gameName, String gameTag, Region region) {
        RiotApiResponseDto.RiotPuuidAndTierInfo riotPuuidAndTierInfo = new RiotApiResponseDto.RiotPuuidAndTierInfo();

        RiotApiResponseDto.RiotPuuid puuid = getPuuid(gameName, gameTag);
        riotPuuidAndTierInfo.setPuuid(puuid.getPuuid());
        String summonerId = getSummonerIdInfo(puuid.getPuuid(), region.name()).getId();
        SummonerInfo.RankInfo[] rankData = getRankData(region.name(), summonerId);
        for (SummonerInfo.RankInfo rankDatum : rankData) {
            if (rankDatum.getQueueType().equals("RANKED_SOLO_5x5")) {
                riotPuuidAndTierInfo.getSoloRank().setTier(rankDatum.getTier());
                riotPuuidAndTierInfo.getSoloRank().setRank(rankDatum.getRank());
                riotPuuidAndTierInfo.getSoloRank().setQueueType(QueueType.SOLO);
            }
            if (rankDatum.getQueueType().equals("RANKED_FLEX_SR")) {
                riotPuuidAndTierInfo.getFreeRank().setTier(rankDatum.getTier());
                riotPuuidAndTierInfo.getFreeRank().setRank(rankDatum.getRank());
                riotPuuidAndTierInfo.getFreeRank().setQueueType(QueueType.FREE);
            }
        }
        return riotPuuidAndTierInfo;
    }

    public RiotApiResponseDto.RiotPuuid getPuuid(String gameName, String gameTag) {
        return accountClient.fetchPuuidAPI(gameName, gameTag, "ASIA");
    }

    public RiotApiResponseDto.RiotSummonerIdInfo getSummonerIdInfo(String puuid, String region) {
        return summonerClient.fetchSummonerIdAPI(puuid, region);
    }


    public SummonerInfo.RankInfo[] getRankData(String region, String summonerId) {
        return summonerClient.fetchRankData(region, summonerId);
    }
}
