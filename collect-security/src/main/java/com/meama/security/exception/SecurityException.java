package com.meama.security.exception;

import org.springframework.http.HttpStatus;

public class SecurityException extends Exception {

    private HttpStatus httpStatus;

    public SecurityException() {
        super();
    }

    public SecurityException(String message) {
        super(message);
    }

    public SecurityException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public SecurityException(String message, Throwable cause) {
        super(message, cause);
    }

    public SecurityException(Throwable cause) {
        super(cause);
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
