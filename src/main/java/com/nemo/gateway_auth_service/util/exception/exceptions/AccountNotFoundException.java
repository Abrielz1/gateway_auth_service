package com.nemo.gateway_auth_service.util.exception.exceptions;

public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(String message) {
        super(message);
    }
}
