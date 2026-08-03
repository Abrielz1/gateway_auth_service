package com.nemo.gateway_auth_service.util.mapper.client.to;

import com.nemo.gateway_auth_service.app.domain.EmailData;
import com.nemo.gateway_auth_service.app.domain.LoginData;
import com.nemo.gateway_auth_service.app.domain.PasswordData;
import com.nemo.gateway_auth_service.app.domain.PhoneData;
import com.nemo.gateway_auth_service.app.domain.User;
import com.nemo.gateway_auth_service.app.domain.entity.enums.RoleType;
import com.nemo.gateway_auth_service.web.model.request.UserRegistrationRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ClientTo {

    private final PasswordEncoder passwordEncoder;

    public User toEntity(UserRegistrationRequestDTO clientRegisterRequestDTO) {

        var user = User.builder()
                .userUUID(UUID.randomUUID())
                .enabled(true)
                .isDeleted(false)
                .roles(Set.of(RoleType.ROLE_CLIENT))
                .build();

        var emailData = EmailData.builder()
                .email(clientRegisterRequestDTO.email())
                .build();

        var phoneData = PhoneData.builder()
                .phone(clientRegisterRequestDTO.phone())
                .build();

        var passwordData = PasswordData.builder()
                .password(passwordEncoder.encode(clientRegisterRequestDTO.password()))
                .timeWhenSet(Instant.now())
                .isActive(true)
                .timeToLive(Instant.now().plusSeconds(31536000L))
                .build();

        var loginData = LoginData.builder()
                .login(clientRegisterRequestDTO.username())
                .build();

        user.addEmailData(emailData);
        user.addPhoneData(phoneData);
        user.addPasswordData(passwordData);
        user.addLoginData(loginData);

        return user;
    }
}
