package com.nemo.gateway_auth_service.util.exception.exceptions;

import java.util.UUID;

public class SecurityBreachAttemptException extends RuntimeException {

    public SecurityBreachAttemptException(String message) {
        super(message);
    }

    public SecurityBreachAttemptException(String message, Long userId, UUID id, String remoteAddr) {

    }
}
