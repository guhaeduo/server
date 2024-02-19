package org.inflearngg.aop;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.inflearngg.aop.error.ErrorCode;
import org.inflearngg.aop.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//controller 에서 발생하는 예외를 처리하는 클래스
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    //예외처리로직
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleValidationException(MethodArgumentNotValidException e) {
        final List<ErrorResponse.FieldError> fieldErrors = bindingFieldErrors(e.getBindingResult());
        return bindingFieldErrors(ErrorCode.INVALID_INPUT_VALUE, fieldErrors);
    }
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponse handleBindException(org.springframework.validation.BindException e) {
        final List<ErrorResponse.FieldError> fieldErrors = bindingFieldErrors(e.getBindingResult());
        return bindingFieldErrors(ErrorCode.INVALID_INPUT_VALUE, fieldErrors);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponse handleHttpClientErrorException(HttpClientErrorException e) throws JsonProcessingException {

        String errorMessage = e.getMessage().substring(e.getMessage().indexOf(":")+3);
        log.info(errorMessage);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(errorMessage);
        log.info("jsonNode : " + jsonNode);

        String message = jsonNode.at("/status/message").asText();
        int status = e.getStatusCode().value();
//        log.info("HttpClientErrorException : " + status1);
        log.info("HttpClientErrorException : " + message);

        List<ErrorResponse.FieldError> fieldErrors = new ArrayList<>();
        fieldErrors.add(ErrorResponse.FieldError.builder()
                .field("HttpClientError")
                .value(message)
                .reason("Riot Api 요청 에러 발생")
                .build());

        return ErrorResponse.builder()
                .code(ErrorCode.Client_INVALID_INPUT_VALUE.code())
                .message(message)
                .status(status)
                .errors(fieldErrors)
                .build();
    }


    private List<ErrorResponse.FieldError> bindingFieldErrors(BindingResult bindingResult) {
        final List<FieldError> errors = bindingResult.getFieldErrors();
        return errors.stream()
                .map(error -> ErrorResponse.FieldError.builder()
                        .field(error.getField())
                        .value((String) error.getRejectedValue())
                        .reason(error.getDefaultMessage())
                        .build())
                .collect(Collectors.toList());
    }


    private ErrorResponse bindingFieldErrors(ErrorCode errorCode, List<ErrorResponse.FieldError> errors) {
        return ErrorResponse.builder()
                .code(errorCode.code())
                .message(errorCode.message())
                .status(errorCode.status())
                .errors(errors)
                .build();
    }

}
