package org.inflearngg.summoner.mapper;

import org.inflearngg.summoner.client.RiotApiResponseDto;
import org.inflearngg.summoner.dto.response.SummonerResponseDto;
import org.inflearngg.summoner.service.SummonerInfo;
import org.springframework.stereotype.Component;

@Component
public class SummonerMapper {

    public SummonerResponseDto.SummonerIdInfo mapToRiotSummonerIdInfo(RiotApiResponseDto.RiotSummonerIdInfo riotSummonerIdInfo) {
        SummonerResponseDto.SummonerIdInfo summonerIdInfo = new SummonerResponseDto.SummonerIdInfo();
        summonerIdInfo.setSummonerId(riotSummonerIdInfo.getId());
        summonerIdInfo.setAccountId(riotSummonerIdInfo.getAccountId());
        summonerIdInfo.setPuuid(riotSummonerIdInfo.getPuuid());
        summonerIdInfo.setName(riotSummonerIdInfo.getName());
        summonerIdInfo.setProfileIconId(riotSummonerIdInfo.getProfileIconId());
        summonerIdInfo.setRevisionDate(riotSummonerIdInfo.getRevisionDate());
        summonerIdInfo.setSummonerLevel(riotSummonerIdInfo.getSummonerLevel());
        return summonerIdInfo;

    }

    public SummonerResponseDto.SummonerData mapToSummonerResponseDto(SummonerInfo.RankInfo[] rankInfos) {
        SummonerResponseDto.SummonerData summonerData = new SummonerResponseDto.SummonerData();

        SummonerResponseDto.RankData soloRank = new SummonerResponseDto.RankData("unRanked", 0, 0, 0, 0);
        SummonerResponseDto.RankData freeRank = new SummonerResponseDto.RankData("unRanked", 0, 0, 0, 0);

        for (SummonerInfo.RankInfo rankInfo : rankInfos) {
            if (rankInfo.getQueueType().equals("RANKED_SOLO_5x5")) { // 솔로랭크
                rankDataSetting(rankInfo, soloRank);
            }

            if (rankInfo.getQueueType().equals("RANKED_FLEX_SR")) { // 자유랭크
                rankDataSetting(rankInfo, freeRank);
            }
        }
        summonerData.setSoloRank(soloRank);
        summonerData.setFreeRank(freeRank);

        return summonerData;
    }

    private static void rankDataSetting(SummonerInfo.RankInfo rankInfo, SummonerResponseDto.RankData soloRank) {
        soloRank.setTier(rankInfo.getTier());
        soloRank.setLevel(rankInfo.getRank());
        soloRank.setPoint(rankInfo.getLeaguePoints());
        soloRank.setWin(rankInfo.getWins());
        soloRank.setLose(rankInfo.getLosses());
    }

}
