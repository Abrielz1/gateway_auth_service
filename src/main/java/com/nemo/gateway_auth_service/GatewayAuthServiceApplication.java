package com.nemo.gateway_auth_service;

import com.nemo.gateway_auth_service.app.domain.dto.JwtProperties;
import com.nemo.gateway_auth_service.util.confguration.EncryptionProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(scanBasePackages = "com.nemo.gateway_auth_service")
@EnableConfigurationProperties({JwtProperties.class, EncryptionProperties.class})
public class GatewayAuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayAuthServiceApplication.class, args);
	}
}
