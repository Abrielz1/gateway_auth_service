package com.nemo.gateway_auth_service.web.model.request;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequestDto(@NotBlank String refreshToken){
}
