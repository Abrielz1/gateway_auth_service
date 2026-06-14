package com.nemo.gateway_auth_service.util.exception.exceptions;

public class AgeRestrictedRequestException extends RuntimeException {

    public AgeRestrictedRequestException(String message) {
        super(message);
    }
}
