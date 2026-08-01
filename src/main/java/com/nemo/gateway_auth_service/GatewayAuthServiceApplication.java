package com.nemo.gateway_auth_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class GatewayAuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayAuthServiceApplication.class, args);
	}
}

