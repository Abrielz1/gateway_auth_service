package com.nemo.gateway_auth_service.util.exception.exceptions;

public class EmptyHeaderException extends  RuntimeException{

    public EmptyHeaderException(String message) {
        super(message);
    }
}
