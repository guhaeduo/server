package org.inflearngg.match.controller;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.inflearngg.match.dto.response.MatchingResponseDto;
import org.inflearngg.match.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @GetMapping("/{puuid}")
    public ResponseEntity<MatchingResponseDto.MatchData> getMatchDataByPuuid(@PathVariable int puuid){
        MatchingResponseDto.MatchData matchData = matchService.getMatchDataByPuuid(puuid);
        return ResponseEntity.ok(matchData);
    }

    @GetMapping("/{puuid}/list")
    public ResponseEntity<List<MatchingResponseDto.MatchData>> getMatchDataListByPuuid(@PathVariable int puuid) {
        List<MatchingResponseDto.MatchData> matchDataList = matchService.getMatchDataListByPuuid(puuid);
        return ResponseEntity.ok(matchDataList);
    }

    }
