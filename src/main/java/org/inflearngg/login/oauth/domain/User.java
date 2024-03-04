package org.inflearngg.login.oauth.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
