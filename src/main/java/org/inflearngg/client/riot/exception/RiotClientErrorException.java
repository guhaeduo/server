package org.inflearngg.client.riot.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpClientErrorException;

import java.nio.charset.Charset;

public class RiotClientErrorException extends HttpClientErrorException {
    public RiotClientErrorException(HttpStatusCode statusCode) {
        super(statusCode);
    }

    public RiotClientErrorException(HttpStatusCode statusCode, String statusText) {
        super(statusCode, statusText);
    }

    public RiotClientErrorException(HttpStatusCode statusCode, String statusText, byte[] body, Charset responseCharset) {
        super(statusCode, statusText, body, responseCharset);
    }

    public RiotClientErrorException(HttpStatusCode statusCode, String statusText, HttpHeaders headers, byte[] body, Charset responseCharset) {
        super(statusCode, statusText, headers, body, responseCharset);
    }

    public RiotClientErrorException(String message, HttpStatusCode statusCode, HttpHeaders headers) {
        super(message, statusCode, null, headers, null, null);
    }


}
