package com.nemo.gateway_auth_service.util.exception.exceptions;

public class SerializationException extends RuntimeException {

    public SerializationException(String message) {
        super(message);
    }

    // ВАЖНЕЙШИЙ КОНСТРУКТОР!
    public SerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
