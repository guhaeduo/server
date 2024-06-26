package org.inflearngg.aop.error;

import lombok.Getter;



public record ErrorCode(String code, String message, int status) {
    public static final ErrorCode INVALID_INPUT_VALUE = new ErrorCode("C001", "Bad Request : INVALID_INPUT_VALUE_입력 값이 올바르지 않습니다.", 400);
    public static final ErrorCode METHOD_NOT_ALLOWED = new ErrorCode("C002", "Bad Request: METHOD_NOT_ALLOWED_허용되지않는 메소드입니다. 입력값의 형식이 올바르지 않습니다.", 405);
    public static final ErrorCode INTERNAL_SERVER_ERROR = new ErrorCode("C003", "Server Error", 500);

    public static final ErrorCode Client_INVALID_INPUT_VALUE = new ErrorCode("C004", "Bad Request : Client_ERROR_입력한 요청 값이 올바르지 않습니다.", 400);

    public static final ErrorCode RIOT_NOT_FOUND = new ErrorCode("C005", "찾을 수 없는 소환사입니다.", 404);
    public static final ErrorCode EMPTY_MATCH_LIST = new ErrorCode("C006", "소환사의 게임 정보를 찾을 수 없습니다.", 404);
    public static final ErrorCode HEADER_NOT_NULL = new ErrorCode("C007", "Bad Request : 요청 헤더 값이 필요합니다. ", 400);

    public static final ErrorCode UNAUTHORIZED = new ErrorCode("C008", "Unauthorized : 토큰이 유효하지 않습니다.", 401);

//    public static final ErrorCode  EXPIRE_EMAIL_CODE = new ErrorCode("C005", "Unauthorized : 만료된 인증번호입니다.", 401);

    public static final ErrorCode EMAIL_NOT_FOUND = new ErrorCode("C009", "Not Found : EMAIL_NOT_FOUND_이메일이 존재하지 않습니다.", 404);

    public static final ErrorCode INVALID_INPUT_LOGIN_VALUE = new ErrorCode("C010", "Bad Request : 이메일 또는 비밀번호를 잘못 입력하셨습니다", 400);
}
