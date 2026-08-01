package com.nemo.gateway_auth_service.util.confguration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({EncryptionProperties.class, EncryptionProperties.class})
public class AppPropertiesConfig {
}
