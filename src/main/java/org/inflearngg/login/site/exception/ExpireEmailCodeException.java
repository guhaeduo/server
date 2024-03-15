package org.inflearngg.login.site.exception;

import org.inflearngg.aop.error.ExceptionErrorMessage;

public class ExpireEmailCodeException extends EmailException{

    public ExpireEmailCodeException() {
        super(ExceptionErrorMessage.EMAIL_AUTHENTICATION_EXPIRED);
    }
}
