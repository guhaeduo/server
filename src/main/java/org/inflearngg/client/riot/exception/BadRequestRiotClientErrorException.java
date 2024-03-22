package org.inflearngg.client.riot.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;

public class BadRequestRiotClientErrorException extends RiotClientErrorException{

    public BadRequestRiotClientErrorException(HttpStatusCode statusCode) {
        super(statusCode);
    }
    public BadRequestRiotClientErrorException(HttpStatusCode statusCode, String statusText) {
        super(statusCode, statusText);
    }

    public BadRequestRiotClientErrorException(String message, HttpStatusCode statusCode, HttpHeaders headers) {
        super(message, statusCode, headers);
    }
}
