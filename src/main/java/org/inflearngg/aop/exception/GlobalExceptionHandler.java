package org.inflearngg.aop.exception;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.inflearngg.aop.error.ErrorCode;
import org.inflearngg.aop.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//controller 에서 발생하는 예외를 처리하는 클래스
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ClientErrorHandler clientErrorHandler;

    //예외처리로직

    /**
     * 컨트롤러 들어오는 값들에 대한 에러 핸드링
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(MethodArgumentNotValidException e) {
        final List<ErrorResponse.FieldError> fieldErrors = bindingFieldErrors(e.getBindingResult());
        return bindingFieldErrors(ErrorCode.INVALID_INPUT_VALUE, fieldErrors);
    }
    // mvc패턴일 경우
//    @ExceptionHandler(BindException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    protected ErrorResponse handleBindException(org.springframework.validation.BindException e) {
//        final List<ErrorResponse.FieldError> fieldErrors = bindingFieldErrors(e.getBindingResult());
//        return bindingFieldErrors(ErrorCode.INVALID_INPUT_VALUE, fieldErrors);
//    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponse handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        final List<ErrorResponse.FieldError> fieldErrors = new ArrayList<>();
        fieldErrors.add(ErrorResponse.FieldError.builder()
                .field(e.getName())
                .value(e.getValue().toString())
                .reason(e.getMessage())
                .build());
        return bindingFieldErrors(ErrorCode.METHOD_NOT_ALLOWED, fieldErrors);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponse handleMissingRequestHeaderException(MissingRequestHeaderException e) {
        final List<ErrorResponse.FieldError> fieldErrors = new ArrayList<>();
        fieldErrors.add(ErrorResponse.FieldError.builder()
                .field(e.getHeaderName())
                .value("null")
                .reason(e.getMessage())
                .build());
        return bindingFieldErrors(ErrorCode.HEADER_NOT_NULL, fieldErrors);
    }

    @ExceptionHandler(MissingPathVariableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponse handleMissingPathVariableException(MissingPathVariableException e) {
        final List<ErrorResponse.FieldError> fieldErrors = new ArrayList<>();
        fieldErrors.add(ErrorResponse.FieldError.builder()
                .field(e.getVariableName())
                .value("null")
                .reason(e.getMessage())
                .build());
        return bindingFieldErrors(ErrorCode.INVALID_INPUT_VALUE, fieldErrors);
    }

    /**
     * Client에서 발생하는 에러 핸들링
     *
     * @throws JsonProcessingException
     */
    @ExceptionHandler(HttpClientErrorException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponse handleHttpClientErrorException(HttpClientErrorException e) throws JsonProcessingException {
        log.info(e.getResponseBodyAsString());
        List<ErrorResponse.FieldError> errors = clientErrorHandler.handleClientError(e.getResponseBodyAsString());
        return bindingFieldErrors(ErrorCode.Client_INVALID_INPUT_VALUE, errors);
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
