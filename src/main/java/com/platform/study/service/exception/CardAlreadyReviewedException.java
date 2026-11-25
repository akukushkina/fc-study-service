package com.platform.study.service.exception;

public class CardAlreadyReviewedException extends RuntimeException {
    public CardAlreadyReviewedException(String message) {
        super(message);
    }
}
