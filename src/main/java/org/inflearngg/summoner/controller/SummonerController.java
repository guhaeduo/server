package org.inflearngg.summoner.controller;

import lombok.RequiredArgsConstructor;
import org.inflearngg.summoner.dto.response.SummonerResponseDto;
import org.inflearngg.summoner.mapper.SummonerMapper;
import org.inflearngg.summoner.service.SummonerInfo;
import org.inflearngg.summoner.service.SummonerService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 전적 검색
//1. 소환사 정보
// 2. (큐)match정보
@RestController
@RequestMapping("/api/summoner")
@RequiredArgsConstructor
public class SummonerController {

    private final SummonerService summonerService;
    private final SummonerMapper summonerMapper;

    //1.소환사 정보
    @GetMapping(value = "/{puuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public SummonerResponseDto.SummonerData getSummonerInfoByPuuid(@PathVariable String puuid) {

        SummonerInfo.SummonerBasicInfo summonerBasicInfo = summonerService.getSummonerBasicInfo(puuid);
        SummonerInfo.RankInfo[] rankInfos = summonerService.getRankData(summonerBasicInfo.getId());
        SummonerResponseDto.SummonerData summonerData = summonerMapper.mapToSummonerResponseDto(summonerBasicInfo, rankInfos);
        System.out.println("[정보가져와] 솔랭 : " + summonerData.getSoloRank() + "  자유랭 : " +  summonerData.getFreeRank());

        return summonerData;
    }

}
