package com.nemo.gateway_auth_service.web.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record ClientLoginRequestDTO(

        @NotBlank
        String clientIdentifier,

        @Size(min = 8, max = 64, message = "password must be not at least 8 characters, and not exceed 64 characters")
        String password) {
}
