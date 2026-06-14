package com.nemo.gateway_auth_service.util.exception.exceptions;

public class RateLimitExceededException extends RuntimeException {
    public RateLimitExceededException(String message) {
        super(message);
    }
}
