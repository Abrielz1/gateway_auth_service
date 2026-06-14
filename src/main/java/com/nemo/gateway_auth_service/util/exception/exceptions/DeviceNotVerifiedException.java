package com.nemo.gateway_auth_service.util.exception.exceptions;

import lombok.Getter;

@Getter
public class DeviceNotVerifiedException extends RuntimeException {

    private final Long userId;

    private final String fingerPrint;

    public DeviceNotVerifiedException(Long userId, String fingerPrint) {
        this.userId = userId;
        this.fingerPrint = fingerPrint;
    }
}
