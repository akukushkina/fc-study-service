package com.platform.study.service.exception;

public class SessionIsNotActiveException extends RuntimeException {
    public SessionIsNotActiveException(String message) {
        super(message);
    }
}
