package org.inflearngg.summoner.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class SummonerRequestDto {


    @Getter
    @Setter
    @NoArgsConstructor
    public static class RegionAndSummonerId {
        private String region;
        private String summonerId;
    }

}
