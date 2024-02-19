package org.inflearngg.aop.error;

import lombok.Getter;


public record ErrorCode(String code, String message, int status) {
    public static final ErrorCode INVALID_INPUT_VALUE = new ErrorCode("C001", "Bad Request : 입력 값이 올바르지 않습니다.", 400);
    public static final ErrorCode METHOD_NOT_ALLOWED = new ErrorCode("C002", "METHOD_NOT_ALLOWED : 허용되지않는 메소드입니다.", 405);
    public static final ErrorCode INTERNAL_SERVER_ERROR = new ErrorCode("C003", "Server Error", 500);

    public static final ErrorCode Client_INVALID_INPUT_VALUE = new ErrorCode("C004", "Bad Request : 입력한 요청 값이  올바르지 않습니다.", 400);


}
