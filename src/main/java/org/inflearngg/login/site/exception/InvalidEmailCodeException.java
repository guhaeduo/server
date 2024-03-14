package org.inflearngg.login.site.exception;

public class InvalidEmailCodeException extends EmailException{
    public InvalidEmailCodeException(String message) {
        super(message);
    }
}
