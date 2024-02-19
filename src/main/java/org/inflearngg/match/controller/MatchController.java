package org.inflearngg.match.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.inflearngg.match.dto.request.MatchRequestDto;
import org.inflearngg.match.dto.response.MatchingResponseDto;
import org.inflearngg.match.mapper.ProcessMatchMapper;
import org.inflearngg.match.service.MatchService;

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
    public MatchingResponseDto getMatchDataListByPuuid(
            @RequestHeader("puuid") String puuid,
            @RequestHeader("queueType") MatchRequestDto.QueueType queueId) {
        LocalDateTime startTime = LocalDateTime.now();
        log.info("요청시간 : " + startTime);
        log.info("queueType : " + queueId.getQueueId());
        String[] matchIdList = matchService.getMatchIdsByPuuid(puuid, queueId.getQueueId());
        MatchingResponseDto matchingResponseDto = new MatchingResponseDto();
        matchingResponseDto.setMatchDataList(mapper.mapToMatchDataList(matchService.getMatchDataList(matchIdList, puuid)));
        matchingResponseDto.setTotalGameCnt(matchIdList.length);
        matchingResponseDto.setQueueType(queueId.name());

        LocalDateTime endTime = LocalDateTime.now();
        log.info("응답시간 : " + endTime);
        log.info("요청 후 응답시간 : " + startTime.until(endTime, ChronoUnit.SECONDS) + "s");
        return matchingResponseDto;
    }

    @GetMapping("/summary")
    @ResponseBody
    public MatchingResponseDto.SummonerRankInfo getSummonerRankInfo(
            @RequestHeader("puuid") String puuid,
            @RequestHeader("queueType") MatchRequestDto.QueueType queueId) throws ExecutionException, InterruptedException {
        String[] matchIdList = matchService.getMatchIdsByPuuid(puuid, queueId.getQueueId());
        return mapper.mapToSummonerRankInfo(matchService.getSummonerSummaryInfo(matchIdList, puuid));
    }

}
