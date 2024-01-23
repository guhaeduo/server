package org.inflearngg.summoner.mapper;

import org.inflearngg.summoner.dto.response.SummonerResponseDto;
import org.inflearngg.summoner.service.SummonerInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
public class SummonerMapper {

    public SummonerResponseDto.SummonerData mapToSummonerResponseDto(SummonerInfo.SummonerBasicInfo summonerBasicInfo, SummonerInfo.RankInfo[] rankInfos) {
        SummonerResponseDto.SummonerData summonerData = new SummonerResponseDto.SummonerData();
        summonerData.setRiotName(summonerBasicInfo.getName());
//        summonerData.setRiotTag(summonerBasicInfo.getRiotTag());
        summonerData.setPuuid(summonerBasicInfo.getPuuid());
        summonerData.setSummonerLevel(summonerBasicInfo.getSummonerLevel());
        summonerData.setProfileIconNumber(summonerBasicInfo.getProfileIconId());


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
