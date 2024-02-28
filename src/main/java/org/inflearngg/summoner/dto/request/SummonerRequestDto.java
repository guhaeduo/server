package org.inflearngg.summoner.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.inflearngg.aop.dto.Region;

public class SummonerRequestDto {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class NameAndTagAndRegion {
        @NotBlank(message = "게임 이름을 입력해주세요.")
        private String gameName;
        @NotBlank(message = "태그를 입력해주세요.")
        private String tagLine;

        @NotNull(message = "지역을 입력해주세요.")
        private Region region;
    }

}
