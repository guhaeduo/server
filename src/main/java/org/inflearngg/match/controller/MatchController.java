package org.inflearngg.match.controller;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.inflearngg.match.dto.request.MatchRequestDto;
import org.inflearngg.match.dto.response.MatchingResponseDto;
import org.inflearngg.match.service.MatchInfo;
import org.inflearngg.match.service.MatchService;
import org.inflearngg.match.service.ProcessingMatchInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

// 전적 검색
// 1. (큐)match정보

@Slf4j
@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;


    // 2. (큐)match정보 , 요청 body에 queueType이 넘어옴
    @GetMapping("/{puuid}/list")
    @ResponseBody
    public List<ProcessingMatchInfo.MatchData> getMatchDataListByPuuid(
            @PathVariable String puuid, @RequestBody MatchRequestDto.QueueType queueType) {
        LocalDateTime startTime = LocalDateTime.now();
        log.info("요청시간 : " + startTime);

        String[] matchIdList = matchService.getMatchIdsByPuuid(puuid, Integer.parseInt(queueType.getQueueType()));
        List<ProcessingMatchInfo.MatchData> matchDataList = matchService.getMatchDataList(matchIdList, puuid);
        LocalDateTime endTime = LocalDateTime.now();
        log.info("응답시간 : " + endTime);
        log.info("요청 후 응답시간 : " + startTime.until(endTime, ChronoUnit.SECONDS) + "s");
        return matchDataList;
    }

}
