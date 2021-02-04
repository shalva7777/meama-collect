package com.meama.security.exception;

import org.springframework.http.HttpStatus;

public class MainException extends Exception {

    private HttpStatus httpStatus;

    public MainException() {
        super();
    }

    public MainException(String message) {
        super(message);
    }

    public MainException(String message, Throwable cause) {
        super(message, cause);
    }

    public MainException(Throwable cause) {
        super(cause);
    }


    public MainException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
