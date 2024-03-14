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
// 수정 필요
    @Email
    String email;

    @NotBlank
    String password;

    @NotBlank
    String code;
}
