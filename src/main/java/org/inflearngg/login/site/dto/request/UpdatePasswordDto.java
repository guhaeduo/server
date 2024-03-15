package org.inflearngg.login.site.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter
@NoArgsConstructor
public class UpdatePasswordDto {
    @NotBlank
    String beforePassword;
    @NotBlank
    String afterPassword;
}
