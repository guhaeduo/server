package org.inflearngg.match.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.inflearngg.aop.dto.Region;
import org.inflearngg.match.dto.request.MatchRequestDto;
import org.inflearngg.match.dto.response.MatchingResponseDto;
import org.inflearngg.match.mapper.ProcessMatchMapper;
import org.inflearngg.match.service.MatchService;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ExecutionException;

// 전적 검색
// 1. (큐)match정보

@Slf4j
@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;
    private final ProcessMatchMapper mapper;


    //헤더로 puuid, 큐타입 받아서 전적정보 가져오기
    @GetMapping("/list")
    @ResponseBody
    @Validated
    public MatchingResponseDto getMatchDataListByPuuid(
            @RequestHeader("puuid") String puuid,
            @RequestHeader("queueType") MatchRequestDto.QueueType queueId,
            @RequestHeader("region") Region region
    ) {
        // 헤더 검증
        if (puuid == null) {
            throw new RuntimeException("puuid 헤더 정보가 잘못되었습니다.");
        }
        String[] matchIdList = matchService.getMatchIdsByPuuid(puuid, queueId.getQueueId(), region.getContinent());
        MatchingResponseDto matchingResponseDto = new MatchingResponseDto();
        matchingResponseDto.setMatchDataList(mapper.mapToMatchDataList(matchService.getMatchDataList(matchIdList, puuid, region.getContinent())));
        matchingResponseDto.setTotalGameCnt(matchingResponseDto.getMatchDataList().size());
        matchingResponseDto.setQueueType(queueId.name());

        return matchingResponseDto;
    }

    @GetMapping("/summary")
    @ResponseBody
    @Validated
    public MatchingResponseDto.SummonerRankInfo getSummonerRankInfo(
            @RequestHeader("puuid") String puuid,
            @RequestHeader("queueType") MatchRequestDto.QueueType queueId,
            @RequestHeader("region") Region region
    ) throws ExecutionException, InterruptedException {
        String[] matchIdList = matchService.getMatchIdsByPuuid(puuid, queueId.getQueueId(), region.getContinent());
        return mapper.mapToSummonerRankInfo(matchService.getSummonerSummaryInfo(matchIdList, puuid, region.getContinent()));
    }

}
