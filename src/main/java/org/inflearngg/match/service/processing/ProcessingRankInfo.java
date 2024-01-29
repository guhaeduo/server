package org.inflearngg.match.service.processing;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProcessingRankInfo {
    @Getter
    @Setter
    @NoArgsConstructor
    public static class SummonerRankInfo {
        private RankInfo info;
        private Rane rane;


        @Getter
        @Setter
        @NoArgsConstructor
        public static class RankInfo {
            private String winningRate;
            private int wins;
            private int loses;
            private String kda;
            private double killAvg;
            private double deathAvg;
            private double assistAvg;
        }

        @Getter
        @Setter
        @NoArgsConstructor
        public static class Rane {
            private RankRaneData all;
            private RankRaneData top;
            private RankRaneData jug;
            private RankRaneData mid;
            private RankRaneData adc;
            private RankRaneData sup;

            @Getter
            @Setter
            @NoArgsConstructor
            public static class RankRaneData {
                private String winningRate;
                private List<RankRaneChampionData> champions;

                @Getter
                @Setter
                @NoArgsConstructor
                public static class RankRaneChampionData {
                    private String championName;
                    private int championIconNumber;
                    private String winningRate;
                    private String csAvg;
                    private int visonScore;
                    private String kda;
                    private String killParticipation;
                }
            }
        }
    }


}
