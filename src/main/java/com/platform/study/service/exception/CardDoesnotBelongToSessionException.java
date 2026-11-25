package com.platform.study.service.exception;

public class CardDoesnotBelongToSessionException extends RuntimeException {
    public CardDoesnotBelongToSessionException(String message) {
        super(message);
    }
}
