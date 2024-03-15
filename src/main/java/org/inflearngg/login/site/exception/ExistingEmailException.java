package org.inflearngg.login.site.exception;

import org.inflearngg.aop.error.ExceptionErrorMessage;

public class ExistingEmailException extends EmailException{
    public ExistingEmailException() {
        super(ExceptionErrorMessage.EMAIL_EXIST);
    }
}
