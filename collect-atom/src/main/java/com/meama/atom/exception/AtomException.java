package com.meama.atom.exception;

import org.springframework.http.HttpStatus;

public class AtomException extends Exception {

    private HttpStatus httpStatus;

    public AtomException() {
        super();
    }

    public AtomException(String message) {
        super(message);
    }

    public AtomException(String message, Throwable cause) {
        super(message, cause);
    }

    public AtomException(Throwable cause) {
        super(cause);
    }


    public AtomException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}

