package com.meama.meamacollect.filesystem.exception;

import org.springframework.http.HttpStatus;

public class FileSystemException extends Exception {

    private HttpStatus httpStatus;

    public FileSystemException() {
        super();
    }

    public FileSystemException(String message) {
        super(message);
    }

    public FileSystemException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public FileSystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileSystemException(Throwable cause) {
        super(cause);
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
