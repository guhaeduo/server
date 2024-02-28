package org.inflearngg.summoner.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.inflearngg.aop.dto.Region;
import org.inflearngg.summoner.dto.request.SummonerRequestDto;
import org.inflearngg.summoner.mapper.SummonerMapper;
import org.inflearngg.summoner.service.SummonerInfo;
import org.inflearngg.summoner.service.SummonerService;
import org.springframework.web.bind.annotation.*;

import static org.inflearngg.summoner.dto.response.SummonerResponseDto.*;


@Slf4j
@RestController
@RequestMapping("/api/summoner")
@RequiredArgsConstructor
public class SummonerController {

    private final SummonerService summonerService;
    private final SummonerMapper summonerMapper;


    @GetMapping("/info")
    public SummonerIdInfo getSummonerIdByNameAndTag(
            @RequestParam("region") Region region,
            @RequestParam("gameName") String gameName,
            @RequestParam("tagLine") String tagLine) {
        return summonerMapper.mapToRiotSummonerIdInfo(
                summonerService.checkAndGetIdList(gameName, tagLine, region));
    }

    @GetMapping("/rank")
    public SummonerData getSummonerRankByPuuid(
            @RequestHeader("region") Region region,
            @RequestHeader("summonerId") String summonerId) {
        SummonerInfo.RankInfo[] rankInfos = summonerService.getRankData(region.name(), summonerId);
        SummonerData summonerData = summonerMapper.mapToSummonerResponseDto(rankInfos);
        return summonerData;
    }

}
