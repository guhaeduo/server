package org.inflearngg.match.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.inflearngg.match.dto.request.MatchRequestDto;
import org.inflearngg.match.dto.response.MatchingResponseDto;
import org.inflearngg.match.mapper.MatchMapper;
import org.inflearngg.match.service.MatchService;
import org.inflearngg.match.service.processing.ProcessingData;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

// 전적 검색
// 1. (큐)match정보

@Slf4j
@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;
    private final MatchMapper mapper;

    // 2. (큐)match정보 , 요청 body에 queueType이 넘어옴
    @GetMapping("/{puuid}/list")
    @ResponseBody
    public MatchingResponseDto getMatchDataListByPuuid(
            @PathVariable String puuid, @RequestBody MatchRequestDto.QueueType queueType) {
        LocalDateTime startTime = LocalDateTime.now();
        log.info("요청시간 : " + startTime);

        String[] matchIdList = matchService.getMatchIdsByPuuid(puuid, Integer.parseInt(queueType.getQueueType()));
        ProcessingData processingData = matchService.getProcessingData(matchIdList, puuid);
        MatchingResponseDto matchingResponseDto = mapper.mapToMatchingResponseDto(processingData);

        LocalDateTime endTime = LocalDateTime.now();
        log.info("응답시간 : " + endTime);
        log.info("요청 후 응답시간 : " + startTime.until(endTime, ChronoUnit.SECONDS) + "s");


        return matchingResponseDto;
    }

}
