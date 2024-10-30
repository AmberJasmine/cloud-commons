package org.example.config.exception.base;

public class TokenException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private int code;

    private String message;

    public TokenException(String message) {
        this.message = message;
    }

    public TokenException(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public TokenException(String message, Throwable e) {
        super(message, e);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
