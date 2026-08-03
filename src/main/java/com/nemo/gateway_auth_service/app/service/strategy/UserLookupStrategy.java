package com.nemo.gateway_auth_service.app.service.strategy;

public interface UserLookupStrategy {

    boolean supports(String identifier);
}
