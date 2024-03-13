package org.inflearngg.login.site.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ResetPasswordDto {

    @Email
    String email;

    @NotBlank
    String password;

    @NotBlank
    String code;
}
