package com.nemo.gateway_auth_service.web.model.response;

import lombok.Builder;
import java.util.List;

@Builder
public record ClientRegistrationResponseDto(String userId, List<String> contacts) {
}
