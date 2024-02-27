package org.inflearngg.oauth.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.inflearngg.oauth.domain.OAuthProvider;

@Getter
@NoArgsConstructor
public class User{
    private String email;
    private String password;
    private OAuthProvider oAuthProvider;

    @Builder
    public User(String email, String password, OAuthProvider oAuthProvider){
        this.email = email;
        this.password = password;
        this.oAuthProvider = oAuthProvider;
    }
}
