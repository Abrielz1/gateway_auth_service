package com.nemo.gateway_auth_service.util.mapper.client.to;

import com.nemo.gateway_auth_service.app.domain.User;
import com.nemo.gateway_auth_service.app.domain.entity.enums.RoleType;
import com.nemo.gateway_auth_service.web.model.request.UserRegistrationRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ClientTo {

    private final PasswordEncoder passwordEncoder;

    public User toEntity(UserRegistrationRequestDTO clientRegisterRequestDTO) {

        return User.builder()
                .userUUID(UUID.randomUUID())
                .enabled(true)
                .isDeleted(false)
                .roles(Set.of(RoleType.ROLE_CLIENT))
                .build();
    }
}
