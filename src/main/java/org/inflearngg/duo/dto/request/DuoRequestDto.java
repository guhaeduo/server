package org.inflearngg.duo.dto.request;

import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

public class DuoRequestDto {

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DuoSearch {
        private String lane; // 동적
        private String queueType; // 동적
        private String tier; // 동적
        @JsonSetter("isRiotVerified")
        private boolean riotVerified; // 동적

        // 테스트용
        public DuoSearch(String lane, String queueType) {
            this.lane = lane;
            this.queueType = queueType;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class DuoPostSave implements DuoPostSaveData {
        @JsonSetter("isLogin")
        @NotNull
        private Boolean login;
        private Long memberId;
        @NotBlank
        private String puuid;
        @NotNull
        private String riotGameName;
        @NotNull
        private String riotGameTag;
        @JsonSetter("isRiotVerified")
        private Boolean riotVerified;
        @NotBlank
        private String needPosition;
        @NotBlank
        private String needQueueType;
        @NotBlank
        private String needTier;
        @Valid
        @NotNull
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
        @NotBlank
        private Long postId;
        @NotBlank
        private String riotGameName;
        @NotBlank
        private String riotGameTag;
        @NotBlank
        private Boolean isRiotVerified;
        @NotBlank
        private String needPosition;
        @NotBlank
        private String needQueueType;
        @NotBlank
        private String needTier;
        @NotBlank
        private Position myPosition;
        @NotBlank
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

    public interface DuoPostSaveData {
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
        @NotBlank
        private Boolean isLogin; // true면 deleteInfo 에 memberId, false면 deleteInfo 에 password
        @NotBlank
        private String deleteInfo;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Position {
        @Valid
        @NotNull
        private Main main;
        private Sub sub;

        //테스트용
        public Position(Main main, Sub sub) {
            this.main = main;
            this.sub = sub;
        }

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Main {
            @NotBlank
            private String lane;
            @NotBlank
            private String championName;
            @NotNull
            private int championIconNumber;
        }

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Sub {
            private String lane;
            private String championName;
            private int championIconNumber;
        }
    }

}
