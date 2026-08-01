package com.nemo.gateway_auth_service.util.exception.exceptions;

public class RevocationFailedException extends RuntimeException {

    public RevocationFailedException(String message) {
        super(message);
    }
}
