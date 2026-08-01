package com.nemo.gateway_auth_service.web.model.response;

import lombok.Builder;

@Builder
public record AuthTokenDto(String accessToken, String refreshToken, String userId) {
}
