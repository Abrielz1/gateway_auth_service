package com.nemo.gateway_auth_service.app.service.strategy;

public interface ClientLookupStrategy {

    boolean supports(String identifier);
}
