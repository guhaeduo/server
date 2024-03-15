package org.inflearngg.login.site.exception;

import static org.inflearngg.aop.error.ExceptionErrorMessage.EMAIL_AUTHENTICATION_NOT_MATCH;

public class InvalidEmailCodeException extends EmailException{
    public InvalidEmailCodeException() {
        super(EMAIL_AUTHENTICATION_NOT_MATCH);
    }
}
