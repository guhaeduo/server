package org.inflearngg.login.site.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SiteLoginMemberInfo {
    @Email
    private String email;
    @NotBlank
    private String password;
}
