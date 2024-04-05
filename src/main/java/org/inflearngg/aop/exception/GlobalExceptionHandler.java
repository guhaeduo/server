package org.inflearngg.aop.exception;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.UnexpectedTypeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.inflearngg.aop.error.ErrorCode;
import org.inflearngg.aop.error.ErrorResponse;
import org.inflearngg.client.riot.exception.BadRequestRiotClientErrorException;
import org.inflearngg.client.riot.exception.EmptyMatchListClientErrorException;
import org.inflearngg.client.riot.exception.NotFoundRiotClientErrorException;
import org.inflearngg.client.riot.exception.RiotClientErrorException;
import org.inflearngg.login.site.exception.EmailException;
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
    private final ObjectMapper objectMapper;

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

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationException(ConstraintViolationException e) {
        final List<ErrorResponse.FieldError> fieldErrors = new ArrayList<>();
        e.getConstraintViolations().forEach(error -> {
            fieldErrors.add(ErrorResponse.FieldError.builder()
                    .field(error.getPropertyPath().toString())
                    .value((String) error.getInvalidValue())
                    .reason(error.getMessage())
                    .build());
        });
        return bindingFieldErrors(ErrorCode.INVALID_INPUT_VALUE, fieldErrors);
    }
    // mvc패턴일 경우
//    @ExceptionHandler(BindException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    protected ErrorResponse handleBindException(org.springframework.validation.BindException e) {
//        final List<ErrorResponse.FieldError> fieldErrors = bindingFieldErrors(e.getBindingResult());
//        return bindingFieldErrors(ErrorCode.INVALID_INPUT_VALUE, fieldErrors);
//    }

    @ExceptionHandler(UnexpectedTypeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponse handleUnexpectedTypeException(UnexpectedTypeException e) {
        final List<ErrorResponse.FieldError> fieldErrors = new ArrayList<>();
        fieldErrors.add(ErrorResponse.FieldError.builder()
                .field("type")
                .value("typeError")
                .reason(e.getMessage())
                .build());
        return bindingFieldErrors(ErrorCode.INVALID_INPUT_VALUE, fieldErrors);
    }

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

    /**
     * Riot API 에러 핸들링
     */
    @ExceptionHandler(NotFoundRiotClientErrorException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ErrorResponse handleNotFoundRiotClientErrorException(RiotClientErrorException e) throws JsonProcessingException {
        List<ErrorResponse.FieldError> errors = riotClientErrors(e.getMessage());
        return bindingFieldErrors(ErrorCode.RIOT_NOT_FOUND, errors);
    }
    @ExceptionHandler(EmptyMatchListClientErrorException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ErrorResponse handleEmptyMatchListClientErrorException(RiotClientErrorException e) throws JsonProcessingException {
        List<ErrorResponse.FieldError> errors = riotClientErrors(e.getMessage());
        return bindingFieldErrors(ErrorCode.EMPTY_MATCH_LIST, errors);
    }


    @ExceptionHandler(BadRequestRiotClientErrorException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponse handleRiotClientErrorException(RiotClientErrorException e) throws JsonProcessingException {
        List<ErrorResponse.FieldError> errors = riotClientErrors(e.getMessage());
        return bindingFieldErrors(ErrorCode.Client_INVALID_INPUT_VALUE, errors);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponse handleHttpClientErrorException(HttpClientErrorException e) throws JsonProcessingException {
        List<ErrorResponse.FieldError> errors = clientErrorHandler.handleClientError(e.getResponseBodyAsString());
        return bindingFieldErrors(ErrorCode.Client_INVALID_INPUT_VALUE, errors);
    }

    /**
     * JWT 토큰 에러 핸들링
     */
    @ExceptionHandler(JwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected ErrorResponse handleJwtException(JwtException e) {
        return ErrorResponse.builder()
                .code(ErrorCode.UNAUTHORIZED.code())
                .message(e.getMessage())
                .status(ErrorCode.UNAUTHORIZED.status())
                .build();
    }

    /**
     * 이메일 에러 핸들링
     */

    @ExceptionHandler(EmailException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ErrorResponse handleEmailException(EmailException e) {
        return ErrorResponse.builder()
                .code(ErrorCode.EMAIL_NOT_FOUND.code())
                .message(e.getMessage())
                .status(ErrorCode.EMAIL_NOT_FOUND.status())
                .build();
    }

    /**
     * 추후 바꿀 핸들링
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponse handleIllegalArgumentException(IllegalArgumentException e) {
        return ErrorResponse.builder()
                .code(ErrorCode.INVALID_INPUT_VALUE.code())
                .message(e.getMessage())
                .status(ErrorCode.INVALID_INPUT_VALUE.status())
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

    private List<ErrorResponse.FieldError> riotClientErrors(String errorMessage) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(errorMessage);
        final List<ErrorResponse.FieldError> fieldErrors = new ArrayList<>();
        log.info(jsonNode.toString());
        JsonNode messageNode = jsonNode.path("status").path("message");

        if (!messageNode.isMissingNode()) { // "message" 키가 존재하는지 확인
            fieldErrors.add(ErrorResponse.FieldError.builder()
                    .field("RiotApiException")
                    .value("null")
                    .reason(messageNode.asText())
                    .build());
        }
        return fieldErrors;

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
