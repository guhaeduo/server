package org.inflearngg.login.site.exception;

import org.inflearngg.aop.error.ExceptionErrorMessage;

public class EmailNotFoundException extends EmailException{
    public EmailNotFoundException() {
        super(ExceptionErrorMessage.EMAIL_NOT_FOUND);
    }
}
