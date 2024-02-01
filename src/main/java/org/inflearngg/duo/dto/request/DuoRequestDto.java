package org.inflearngg.duo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class DuoRequestDto {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class DuoSearch {
        private int pageIndex;
        private int pageSize; // 고정
        private String Lane;
        private String queueType;
        private String tier;
        private boolean isRiotVerified;


    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class DuoPostSave {
        private String riotGameName;
        private String riotGameTag;
        private boolean isRiotVerified;
        private String needPosition;
        private String needQueueType;
        private String needTier;
        private Position myPosition;
        private boolean isMicOn;
        private String memo;
        private String password;



    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class DuoPostUpdate {
        private Long postId;
        private String riotGameName;
        private String riotGameTag;
        private boolean isRiotVerified;
        private String needPosition;
        private String needQueueType;
        private String needTier;
        private Position myPosition;
        private boolean isMicOn;
        private String memo;
        private String passwordCheck;


    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class DuoPostDelete {
        private Long postId;
        private String deletePw;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Position {
        private Main main;
        private Sub sub;

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
