package org.inflearngg.match.service.processing;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProcessingData {
    private ProcessingRankInfo.SummonerRankInfo summonerRankInfo;
    private List<ProcessingMatchInfo.MatchData> matchDataList;

}
