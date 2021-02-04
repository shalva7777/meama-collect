package com.meama.atom.exception;

public class AtomRuntimeException extends RuntimeException {

    public AtomRuntimeException() {
        super();
    }

    public AtomRuntimeException(String message) {
        super(message);
    }

    public AtomRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public AtomRuntimeException(Throwable cause) {
        super(cause);
    }
}
