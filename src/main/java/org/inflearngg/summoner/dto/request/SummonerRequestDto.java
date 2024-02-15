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
//        JP("jp1"),PH("ph2"), SG("sg2"), TH("th2"), TW("tw2"), VN("vn2"), NA("na1"), BR("br1"), LAN("la1"), LAS("la2"), OCE("oc1"), EUW("euw1"), EUNE("eun1"), RU("ru"), TR("tr1");
//        private String region;
//        Region(String region) {
//            this.region = region;
//        }
//        public String getRegion() {
//            return region;
//        }
    }
}
