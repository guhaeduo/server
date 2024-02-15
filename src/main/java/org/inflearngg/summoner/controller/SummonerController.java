package org.inflearngg.summoner.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.inflearngg.summoner.dto.request.SummonerRequestDto;
import org.inflearngg.summoner.dto.response.SummonerResponseDto;
import org.inflearngg.summoner.mapper.SummonerMapper;
import org.inflearngg.summoner.service.SummonerInfo;
import org.inflearngg.summoner.service.SummonerService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api/summoner")
@RequiredArgsConstructor
public class SummonerController {

    private final SummonerService summonerService;
    private final SummonerMapper summonerMapper;


    @GetMapping()
    public SummonerResponseDto.SummonerData getSummonerInfoByPuuid(
            @RequestHeader("region") String region,
            @RequestHeader("summonerId") String summonerId) {

//        SummonerInfo.SummonerBasicInfo summonerBasicInfo = summonerService.getSummonerBasicInfo(puuid);
        log.info("region : " + region + " summonerId : " + summonerId);
        SummonerInfo.RankInfo[] rankInfos = summonerService.getRankData(region, summonerId);
        SummonerResponseDto.SummonerData summonerData = summonerMapper.mapToSummonerResponseDto(rankInfos);
        System.out.println("[정보가져와] 솔랭 : " + summonerData.getSoloRank() + "  자유랭 : " +  summonerData.getFreeRank());

        return summonerData;
    }

}
