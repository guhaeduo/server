package org.inflearngg.duo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.inflearngg.duo.dto.request.DuoRequestDto;

import java.util.List;

public class DuoResponseDto {
    @Getter
    @Setter
    @NoArgsConstructor
    public static class PageInfo{
        private int totalPage;
        private int currentPage;
        private int pageSize;
        private int totalElements;
        private boolean hasNext;
        private boolean hasPrevious;
        private List<DuoSearch> duoSearchList;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class DuoSearch {
        private int postId;
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

    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Position {
        private DuoRequestDto.Position.Main main;
        private DuoRequestDto.Position.Sub sub;

        @Getter
        @Setter
        @AllArgsConstructor
        public static class Main {
            private String lane;
            private String championName;
            private int championIconNumber;
        }

        @Getter
        @Setter
        @AllArgsConstructor
        public static class Sub {
            private String lane;
            private String championName;
            private int championIconNumber;
        }
    }
}
