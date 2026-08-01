package com.nemo.gateway_auth_service.app.domain.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;
import java.time.Duration;

@ConfigurationProperties(prefix = "app.jwt")
public record JwtProperties(
        String secret,
        Expiration expiration
) {
    public record Expiration(
            Duration access,
            Duration refresh
    ) {}
}
