package org.inflearngg.aop;

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

import java.util.List;
import java.util.stream.Collectors;

//controller 에서 발생하는 예외를 처리하는 클래스
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





    private List<ErrorResponse.FieldError> bindingFieldErrors(BindingResult bindingResult) {
        final List<FieldError> errors = bindingResult.getFieldErrors();
        return errors.stream()
                .map(error -> ErrorResponse.FieldError.builder()
                        .field(error.getField())
                        .value((String)error.getRejectedValue())
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
