package org.inflearngg.aop.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.inflearngg.aop.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClientErrorHandler {

    private final ObjectMapper objectMapper;

    public ErrorResponse handleClientError(String errorMessage) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(errorMessage);
        ErrorResponse errorResponse = new ErrorResponse();
        //status 가 있다면 riot api 에러
        if (jsonNode.has("status")) {
            String message = jsonNode.at("/status/message").asText();
            int code = jsonNode.at("/status/status_code").asInt();
            errorResponse.setCode("RiotApiException");
            errorResponse.setMessage(message);
            errorResponse.setStatus(code);
        }
        // error 가 있다면 kakao api 에러
        if (jsonNode.has("error")) {
            String message = jsonNode.at("/error_description").asText();
            String code = jsonNode.at("/error_code").asText();
            errorResponse.setCode("KakaoApiException [code] : " + code);
            errorResponse.setMessage(message);
            errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        }
        return errorResponse;
    }
}


