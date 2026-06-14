package com.nemo.gateway_auth_service.util.exception.exceptions;

public class SessionNotFoundException extends RuntimeException {

    public SessionNotFoundException(String message) {
        super(message);
    }
}
