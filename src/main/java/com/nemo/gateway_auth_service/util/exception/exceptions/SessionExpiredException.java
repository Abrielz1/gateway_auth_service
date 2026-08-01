package com.nemo.gateway_auth_service.util.exception.exceptions;

public class SessionExpiredException extends RuntimeException {

    public SessionExpiredException(String message) {
        super(message);
    }
}
