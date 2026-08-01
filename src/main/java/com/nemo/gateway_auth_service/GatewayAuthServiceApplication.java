package com.nemo.gateway_auth_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.nemo.gateway_auth_service")
public class GatewayAuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayAuthServiceApplication.class, args);
	}
}

