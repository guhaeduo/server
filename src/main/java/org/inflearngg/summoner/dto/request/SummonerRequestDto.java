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

    public static enum Region {
        kr, jp1, ph2, sg2, th2, tw2, vn2, na1, br1, la1, la2, oc1, euw1, eun1, ru, tr1;
        public String getRegion() {
            return this.name();
        }
    }
}
