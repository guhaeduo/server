package org.inflearngg.duo.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import lombok.*;
import org.inflearngg.duo.dto.request.DuoRequestDto;

import java.util.List;

import static org.inflearngg.duo.dto.response.DuoResponseDto.Position.*;

public class DuoResponseDto {


    // 동적 쿼리로 나온 결과물
    @Getter
    @Setter
    public static class DuoInfo {
        private Long postId;
        private String riotGameName;
        private String riotGameTag;
        private String pUuid;
        private boolean isRiotVerified;
        private String needPosition;
        private String needQueueType;
        private String needTier;
        private Position myPosition;
        private boolean isMicOn;
        private String memo;

        public DuoInfo(Long postId, String riotGameName, String riotGameTag, String pUuid, boolean isRiotVerified, String needPosition, String needQueueType, String needTier, String mainLane, String mainChamp, int mainChampIconNum,String subLane, String subChamp, int subChampIconNum, boolean isMicOn, String memo) {
            this.postId = postId;
            this.riotGameName = riotGameName;
            this.riotGameTag = riotGameTag;
            this.pUuid = pUuid;
            this.isRiotVerified = isRiotVerified;
            this.needPosition = needPosition;
            this.needQueueType = needQueueType;
            this.needTier = needTier;
            this.myPosition = new Position(new Main(mainLane, mainChamp, mainChampIconNum), new Sub(subLane, subChamp, subChampIconNum));
            this.isMicOn = isMicOn;
            this.memo = memo;
        }

        public void setMyPosition(String myMainLane, String myMainChampionName, int myMainChampionIconNumber, String mySubLane, String mySubChampionName, int mySubChampionIconNumber) {
            this.myPosition = new Position(new Main(myMainLane, myMainChampionName, myMainChampionIconNumber), new Sub(mySubLane, mySubChampionName, mySubChampionIconNumber));
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Position {
        private Main main;
        private Sub sub;


        @Getter
        @Setter
        public static class Main {
            private String lane;
            private String championName;
            private int championIconNumber;

            public Main(String lane, String championName, int championIconNumber) {
                this.lane = lane;
                this.championName = championName;
                this.championIconNumber = championIconNumber;
            }
        }

        @Getter
        @Setter
        public static class Sub {
            private String lane;
            private String championName;
            private int championIconNumber;

            public Sub(String lane, String championName, int championIconNumber) {
                this.lane = lane;
                this.championName = championName;
                this.championIconNumber = championIconNumber;
            }
        }
    }
}
