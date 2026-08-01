package com.nemo.gateway_auth_service.app.service.strategy.impl;

import com.nemo.gateway_auth_service.app.service.strategy.ClientLookupStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoginStrategyImpl implements ClientLookupStrategy {

    @Override
    public boolean supports(String identifier) {
        return !identifier.contains("@") && !identifier.matches("^\\+?[0-9\\-\\(\\)\\s]+$") ;
    }
}
