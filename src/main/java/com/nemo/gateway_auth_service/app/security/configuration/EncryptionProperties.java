package com.nemo.gateway_auth_service.app.security.configuration;

import org.springframework.boot.context.properties.bind.Name;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.encryption")
public record EncryptionProperties(@Name("secret-key") String secretKey) {
}
