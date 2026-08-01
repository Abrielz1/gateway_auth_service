package com.nemo.gateway_auth_service.web.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record ClientLogoutRequestDto(@NotBlank String refreshToken) {
}
