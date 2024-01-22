package org.inflearngg.match.service;

import lombok.RequiredArgsConstructor;
import org.inflearngg.match.dto.response.MatchingResponseDto;
import org.inflearngg.match.repository.MatchRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepository;

    public MatchingResponseDto.MatchData getMatchDataByPuuid(int puuid) {
        return null;
    }

    public List<MatchingResponseDto.MatchData> getMatchDataListByPuuid(int puuid) {
        return null; // 임시 코드

    }
}
