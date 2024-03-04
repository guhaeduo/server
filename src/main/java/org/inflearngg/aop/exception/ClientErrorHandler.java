package org.inflearngg.aop.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.inflearngg.aop.error.ErrorCode;
import org.inflearngg.aop.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClientErrorHandler {

    private final ObjectMapper objectMapper;

    public ErrorCode handleClientError(String errorMessage) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(errorMessage);

        //status 가 있다면 riot api 에러
        if (jsonNode.has("status")) {
            String message = jsonNode.at("/status/message").asText();
            int code = jsonNode.at("/status/status_code").asInt();
            return new ErrorCode("RiotApiException", message, code);
        }
        // error 가 있다면 kakao api 에러
        if (jsonNode.has("error")) {
            String message = jsonNode.at("/error_description").asText();
            String code = jsonNode.at("/error_code").asText();
            return new ErrorCode("SocialLoginException", message, 400);
        }
        // error 가 있다면 discord api 에러
        else {
            String message = jsonNode.at("/message").asText();
            return new ErrorCode("DiscordApiException", message, 400);
//            return ErrorCode.Client_INVALID_INPUT_VALUE;
        }
    }
}


