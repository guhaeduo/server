package org.inflearngg.login.jwt.mapper;

import lombok.extern.slf4j.Slf4j;
import org.inflearngg.login.jwt.dto.LoginResponseDto;
import org.inflearngg.login.jwt.token.AuthToken;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoginMapper {

    public LoginResponseDto mapToAuthToken(AuthToken authToken) {
        log.info("mapToAuthToken");
        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setTokenType(authToken.getTokenType());
//        loginResponseDto.setMemberId(authToken.getMemberId());
        return loginResponseDto;
    }

}
