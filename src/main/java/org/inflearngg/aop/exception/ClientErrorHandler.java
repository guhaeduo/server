package org.inflearngg.aop.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.inflearngg.aop.error.ErrorCode;
import org.inflearngg.aop.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ClientErrorHandler {

    private final ObjectMapper objectMapper;

    public List<ErrorResponse.FieldError> handleClientError(String errorMessage) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(errorMessage);
        final List<ErrorResponse.FieldError> fieldErrors = new ArrayList<>();

        //status 가 있다면 riot api 에러
        if (jsonNode.has("status")) {
            String message = jsonNode.at("/status/message").asText();
            int code = jsonNode.at("/status/status_code").asInt();
            fieldErrors.add(ErrorResponse.FieldError.builder()
                    .field("RiotApiException")
                    .value("null")
                    .reason(message)
                    .build());
        }
        // error 가 있다면 kakao api 에러
        if (jsonNode.has("error")) {
            String message = jsonNode.at("/error_description").asText();
            String code = jsonNode.at("/error_code").asText();
            fieldErrors.add(ErrorResponse.FieldError.builder()
                    .field("SocialLoginApiException")
                    .value("null")
                    .reason(message)
                    .build());
        }
        // error 가 있다면 discord api 에러
        return fieldErrors;
    }
}


