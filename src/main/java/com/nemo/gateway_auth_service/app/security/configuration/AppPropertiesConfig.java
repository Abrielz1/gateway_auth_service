package com.nemo.gateway_auth_service.app.security.configuration;

import com.nemo.gateway_auth_service.app.domain.dto.JwtProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
        JwtProperties.class,
        EncryptionProperties.class,
        RedisKeysProperties.class
})
public class AppPropertiesConfig {
}
