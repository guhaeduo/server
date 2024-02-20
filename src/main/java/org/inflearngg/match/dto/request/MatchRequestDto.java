package org.inflearngg.match.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class MatchRequestDto {


    @Getter
    public static enum QueueType {
        ALL(0),
        NORMAL(490),
        SOLO(420),
        FREE(440);

        private final int queueId;

        QueueType(int queueId) {
            this.queueId = queueId;
        }
    }
//The AMERICAS routing value serves NA, BR, LAN and LAS.
//The ASIA routing value serves KR and JP.
// The EUROPE routing value serves EUNE, EUW, TR and RU.
// The SEA routing value serves OCE, PH2, SG2, TH2, TW2 and VN2.
    public static enum Region {

        na1("AMERICAS"),
        br1("AMERICAS"),
        la1("AMERICAS"),
        la2("AMERICAS"),

        kr("ASIA"),
        jp1("ASIA"),

        eun1("EUROPE"),
        euw1("EUROPE"),
        tr1("EUROPE"),
        ru("EUROPE"),

        oc1("SEA"),
        ph2("SEA"),
        sg2("SEA"),
        th2("SEA"),
        tw2("SEA"),
        vn2("SEA");

        private final String continent;

        Region(String continent) {
            this.continent = continent;
        }

        public String getContinent() {
            return continent;
        }

    }

}
