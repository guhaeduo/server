package org.inflearngg.login.site.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Setter
@Getter
@NoArgsConstructor
public class EmailCodeVerifyDto {

    @Email
    String email;

    @NotBlank @Size(min = 6, max = 6)
    String code;
}
