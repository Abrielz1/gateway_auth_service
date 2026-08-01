package com.nemo.gateway_auth_service.util.confguration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.encryption")
public record EncryptionProperties(String secretKey) {
}
