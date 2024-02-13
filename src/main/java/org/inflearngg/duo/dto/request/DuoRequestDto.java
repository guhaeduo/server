package org.inflearngg.duo.dto.request;

import com.fasterxml.jackson.annotation.JsonSetter;
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
    public static class DuoPostSave implements DuoPostSaveData {
        @JsonSetter("isLogin")
        private Boolean login;
        private Long memberId;
        private String puuid;
        private String riotGameName;
        private String riotGameTag;
        @JsonSetter("isRiotVerified")
        private Boolean riotVerified;
        private String needPosition;
        private String needQueueType;
        private String needTier;
        private Position myPosition;
        @JsonSetter("isMicOn")
        private Boolean micOn;
        private String memo;
        private String password;

        @Override
        public Boolean isRiotVerified() {
            return this.riotVerified;
        }

        @Override
        public Boolean isMicOn() {
            return this.micOn;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class DuoPostUpdate implements DuoPostSaveData {
        private Long postId;
        private String riotGameName;
        private String riotGameTag;
        private Boolean isRiotVerified;
        private String needPosition;
        private String needQueueType;
        private String needTier;
        private Position myPosition;
        private Boolean isMicOn;
        private String memo;
        // 체크리스트
        private Long memberId;
        private String passwordCheck;

        @Override
        public Boolean isRiotVerified() {
            return this.isRiotVerified;
        }

        @Override
        public Boolean isMicOn() {
            return this.isMicOn;
        }
    }
    public interface DuoPostSaveData{
        String getRiotGameName();
        String getRiotGameTag();
        Boolean isRiotVerified();
        String getNeedPosition();
        String getNeedQueueType();
        String getNeedTier();
        Position getMyPosition();
        Boolean isMicOn();
        String getMemo();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class DuoPostDelete {
        private Boolean isLogin; // true면 deleteInfo 에 memberId, false면 deleteInfo 에 password
        private String deleteInfo;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Position {
        private Main main;
        private Sub sub;
        //테스트용
        public Position(Main main, Sub sub) {
            this.main = main;
            this.sub = sub;
        }

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
