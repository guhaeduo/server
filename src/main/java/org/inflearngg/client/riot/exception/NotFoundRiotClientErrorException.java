package org.inflearngg.client.riot.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;

public class NotFoundRiotClientErrorException extends RiotClientErrorException{
    public NotFoundRiotClientErrorException(HttpStatusCode statusCode, String statusText) {
        super(statusCode, statusText, null, null);
    }

    public NotFoundRiotClientErrorException(HttpStatusCode statusCode) {
        super(statusCode, "소환사 정보를 찾을 수 없습니다.");
    }

    public NotFoundRiotClientErrorException(String message, HttpStatusCode statusCode, HttpHeaders headers) {
        super(message, statusCode, headers);
    }
}
