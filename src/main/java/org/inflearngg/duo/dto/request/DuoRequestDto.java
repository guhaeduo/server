package org.inflearngg.duo.dto.request;

import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.inflearngg.aop.dto.Region;
import org.inflearngg.duo.dto.Lane;
import org.inflearngg.duo.dto.QueueType;
import org.inflearngg.duo.dto.Tier;

@Slf4j
public class DuoRequestDto {

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DuoSearch {
        @NotNull
        private int page; // 동적
        private Lane lane; // 동적
        private String queueType;
        private Tier tier; // 동적
        @NotNull
        @JsonSetter("isRiotVerified")
        private Boolean isRiotVerified; // 동적

        public int getPage() {
            if (this.page <= 0)
                throw new IllegalArgumentException("페이지 번호가 잘못되었습니다.");
            return this.page;
        }

        public String getLane() {
            if (this.lane == null || this.lane == Lane.ALL)
                return null;
            return this.lane.name();
        }

        public String getQueueType() {
            QueueType qt = QueueType.findByDescription(this.queueType, "queueType");
            if (this.queueType == null || qt == QueueType.ALL)
                throw new IllegalArgumentException("queueType 이 잘못되었습니다.");
            return qt.name();
        }

        public String getTier() {
            if (this.tier == null || this.tier == Tier.ALL)
                return null;
            return this.tier.name();
        }

        public Boolean isRiotVerified() {
            return this.isRiotVerified;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class DuoPostSave implements DtoDuoPost {

        @NotBlank
        private String region;
        @NotNull
        private String riotGameName;
        @NotNull
        private String riotGameTag;

        @NotBlank
        private String needPosition;

        @NotBlank
        private String queueType; //  SOLO, FREE, NORMAL, ABYSS
        @NotBlank
        private String myMainLane; // ALL, TOP, JUG, MID, ADC, SUP

        private String myMainChampionName;
        @NotBlank
        private String mySubLane; //  ALL, TOP, JUG, MID, ADC, SUP

        private String mySubChampionName;

        @Size(max = 100)
        private String memo;
        //회원시 memberId, 비회원시 password
        private String password;
        @NotNull
        @JsonSetter("isMicOn")
        private Boolean isMicOn; // 마이크 여부(디폴트 false)
        @NotNull
        @JsonSetter("isRiotVerified")
        private Boolean isRiotVerified; // 라이엇 인증 여부(디폴트 false)

        public Region getRegion() {
            return Region.findByDescription(this.region, "region");
        }
        @Override
        public Boolean isRiotVerified() {
            return this.isRiotVerified;
        }

        @Override
        public Boolean isMicOn() {
            return this.isMicOn;
        }
        public String getQueueType() {
            return QueueType.findByDescription(this.queueType, "queueType").name();
        }

        public String getNeedPosition() {
            return Lane.findByDescription(this.needPosition, "needPosition").name();
        }
        public String getMyMainLane() {
            return Lane.findByDescription(this.myMainLane, "myMainLane").name();
        }

        public String getMySubLane() {
            return Lane.findByDescription(this.mySubLane, "mySubLane").name();
        }


    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class DuoPostUpdate implements DtoDuoPost {
        @NotNull
        @JsonSetter("isGuestPost")
        private Boolean isGuestPost;
        @NotBlank
        private String region;
        @NotBlank
        private String riotGameName;
        @NotBlank
        private String riotGameTag;
        @NotNull
        @JsonSetter("isRiotVerified")
        private Boolean isRiotVerified;
        @NotBlank
        private String needPosition;
        @NotBlank
        private String queueType;
        @NotBlank
        private String myMainLane; // TOP, JUNGLE, MID, BOT, SUP

        private String myMainChampionName;
        @NotBlank
        private String mySubLane; // TOP, JUNGLE, MID, BOT, SUP

        private String mySubChampionName;
        @NotNull
        @JsonSetter("isMicOn")
        private Boolean isMicOn;
        @Size(max = 100)
        private String memo;

        private String passwordCheck;

        public Region getRegion() {
            return Region.findByDescription(this.region, "region");
        }
        @Override
        public Boolean isRiotVerified() {
            return this.isRiotVerified;
        }

        @Override
        public Boolean isMicOn() {
            return this.isMicOn;
        }

        public String getQueueType() {
            return QueueType.findByDescription(this.queueType, "queueType").name();
        }

        public String getNeedPosition() {
            return Lane.findByDescription(this.needPosition, "needPosition").name();
        }
        public String getMyMainLane() {
            return Lane.findByDescription(this.myMainLane, "myMainLane").name();
        }

        public String getMySubLane() {
            return Lane.findByDescription(this.mySubLane, "mySubLane").name();
        }
    }

    public interface DtoDuoPost {
        String getRiotGameName();

        String getRiotGameTag();

        Boolean isRiotVerified();

        String getNeedPosition();

        String getQueueType();

        String getMyMainLane();

        String getMyMainChampionName();

        String getMySubLane();

        String getMySubChampionName();

        Boolean isMicOn();

        String getMemo();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class DuoPostDelete {
        @NotNull
        @JsonSetter("isGuestPost")
        private Boolean isGuestPost;
        private String PasswordCheck;
    }

}
