package org.inflearngg.match.controller;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.inflearngg.match.dto.request.MatchRequestDto;
import org.inflearngg.match.dto.response.MatchingResponseDto;
import org.inflearngg.match.service.MatchInfo;
import org.inflearngg.match.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 전적 검색
//1. 소환사 정보
// 2. (큐)match정보
@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;


    // 2. (큐)match정보 , 요청 body에 queueType이 넘어옴
    @GetMapping("/{puuid}/list")
    public ResponseEntity<List<MatchingResponseDto.MatchData>> getMatchDataListByPuuid(
            @PathVariable String puuid, @RequestBody MatchRequestDto.QueueType queueType) {
        String[] matchIdsByPuuid = matchService.getMatchIdsByPuuid(puuid, Integer.parseInt(queueType.getQueueType()));
//        List<MatchingResponseDto.MatchData> matchDataList = matchService.getMatchDataListByPuuid(puuid);
        return ResponseEntity.ok().body(null);
    }

}
