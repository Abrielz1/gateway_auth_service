package com.nemo.gateway_auth_service.util.confguration;

import com.nemo.gateway_auth_service.app.domain.dto.JwtProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({EncryptionProperties.class, JwtProperties.class})
public class AppPropertiesConfig {
}
