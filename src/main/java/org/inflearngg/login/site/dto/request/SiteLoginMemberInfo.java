package org.inflearngg.login.site.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SiteLoginMemberInfo {

    private String email;
    private String password;
}
