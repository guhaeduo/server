package org.inflearngg.match.service.processing;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProcessingData {
    private ProcessingRankInfo.SummonerRankInfo summonerRankInfo;
    private ProcessingMatchInfo.MatchData matchData;
}
