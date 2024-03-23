package org.inflearngg.summoner.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.inflearngg.aop.dto.Region;
import org.inflearngg.summoner.dto.request.SummonerRequestDto;
import org.inflearngg.summoner.mapper.SummonerMapper;
import org.inflearngg.summoner.service.SummonerInfo;
import org.inflearngg.summoner.service.SummonerService;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

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
            @Valid SummonerRequestDto.NameAndTagAndRegion requestDto) {
        return summonerMapper.mapToRiotSummonerInfo(
                summonerService.checkAndGetIdList(requestDto.getGameName(), requestDto.getTagLine(), requestDto.getRegion()));
    }

    @GetMapping("/rank")
    public SummonerData getSummonerRankByPuuid(
            @RequestHeader("region") @Valid Region region,
            @RequestHeader("summonerId") String summonerId) {
        return summonerMapper.mapToSummonerResponseDto(summonerService.getRankData(region.name(), summonerId));
    }

}
