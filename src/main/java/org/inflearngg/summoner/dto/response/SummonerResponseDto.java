package org.inflearngg.summoner.dto.response;

import lombok.*;


@Getter
@Setter
public class SummonerResponseDto {

    @Setter
    @Getter
    @NoArgsConstructor
    public static class SummonerData {
        // front와 중복 되어 제거
//        private String riotName;
//        private String riotTag;
//        private String puuid;
        private RankData freeRank;
        private RankData soloRank;
//        private int summonerLevel;
//        private int profileIconNumber;
    }

    @Setter
    @Getter
    @AllArgsConstructor
    public static class RankData {
        private String tier;
        private int level;
        private int point;
        private int win;
        private int lose;

        public void setLevel(String level) {
            this.level = rankLevel.valueOf(level).getLevel();
        }
    }

    enum rankLevel {
        I(1), II(2), III(3), IV(4);
        private int level;

        rankLevel(int level) {
            this.level = level;
        }

        public int getLevel() {
            return level;
        }

    }

}
