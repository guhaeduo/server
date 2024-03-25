package org.inflearngg.health.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.inflearngg.aop.dto.Region;
import org.springframework.validation.annotation.Validated;

public class RequestDto {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ValidCheck {
        @Valid
        @NotNull
        private SomethingObject somethingObject;
        @NotNull
        private Region region;
        @NotBlank
        private String password;
        @NotNull
        private Integer parentId;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @Validated
    public static class ValidCheck2 {

        private String password;

        public ValidCheck2(String password) {
            this.password = password;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SomethingObject {
        @NotNull
        private Integer id;
    }

}
