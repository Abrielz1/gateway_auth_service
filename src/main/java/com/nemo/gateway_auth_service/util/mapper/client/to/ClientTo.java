package com.nemo.gateway_auth_service.util.mapper.client.to;

import com.nemo.gateway_auth_service.app.domain.entity.child.Admin;
import com.nemo.gateway_auth_service.app.domain.entity.enums.RoleType;
import com.nemo.gateway_auth_service.web.model.request.ClientRegistrationRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ClientTo {

    private final PasswordEncoder passwordEncoder;


    public Admin toEntity(ClientRegistrationRequestDTO clientRegisterRequestDTO) {

        LocalDate dateOfBirth;
        try {
            dateOfBirth = LocalDate.parse(clientRegisterRequestDTO.dateOfBirth(), DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Неверный формат даты рождения. Ожидается YYYY-MM-DD", e);
        }

        return Admin.builder()
                .userUUID(UUID.randomUUID())
                .dateOfBirth(dateOfBirth)
                .enabled(true)
                .isDeleted(false)
                .isBanned(false)
                .roles(Set.of(RoleType.ROLE_CLIENT))
                .build();
    }
}
