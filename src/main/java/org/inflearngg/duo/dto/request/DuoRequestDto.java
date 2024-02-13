package org.inflearngg.duo.dto.request;

import lombok.*;

public class DuoRequestDto {


    @Data
    public static class DuoSearch {
        private String lane; // 동적
        private String queueType; // 동적
        private String tier; // 동적
        private boolean isRiotVerified; // 동적


    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class DuoPostSave {
        private boolean isLogin;
        private Long memberId;
        private String puuid;
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
        // 체크리스트
        private String memberId;
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
