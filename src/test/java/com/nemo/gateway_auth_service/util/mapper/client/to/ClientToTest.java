package com.nemo.gateway_auth_service.util.mapper.client.to;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import com.nemo.gateway_auth_service.app.domain.EmailData;
import com.nemo.gateway_auth_service.app.domain.LoginData;
import com.nemo.gateway_auth_service.app.domain.PasswordData;
import com.nemo.gateway_auth_service.app.domain.PhoneData;
import com.nemo.gateway_auth_service.app.domain.User;
import com.nemo.gateway_auth_service.app.domain.entity.enums.RoleType;
import com.nemo.gateway_auth_service.web.model.request.UserRegistrationRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class ClientToTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ClientTo clientTo;

    UserRegistrationRequestDTO userDto;

    @BeforeEach
    void setUp() {
        userDto = UserRegistrationRequestDTO.builder()
                .username("john_doe")
                .email("john@example.com")
                .password("superSecret123")
                .phone("+79991234567")
                .build();

        when(passwordEncoder.encode(any())).thenReturn("$2a$12$hashedPasswordStub");
    }

    @Test
    @DisplayName("Должен создать User с корректным UUID, enabled, isDeleted, roles")
    void WhenGivenIdentifyData_ThenToEntityCreatesUser() {

        User user = this.clientTo.toEntity(userDto);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isNull();
        assertThat(user.getUserUUID()).isNotNull();
        assertThat(user.getIsDeleted()).isNotNull();
        assertThat(user.getIsDeleted()).isFalse();
        assertThat(user.getEnabled()).isNotNull();
        assertThat(user.getEnabled()).isTrue();
        assertThat(user.getRoles()).isNotNull();
        assertThat(user.getRoles()).isNotEmpty();
        assertThat(user.getRoles().size()).isEqualTo(1);
        assertThat(user.getRoles()).containsExactly(RoleType.ROLE_CLIENT);
    }

    @Test
    @DisplayName("Должен создать EmailData с email из DTO и двусторонней связью")
    void WhenGivenEmailInDto_ThenToEntityCreatesEmailDataWithBiLink() {

        User user = this.clientTo.toEntity(userDto);

        assertThat(user.getUserEmails()).isNotNull();
        assertThat(user.getUserEmails()).isNotEmpty();
        assertThat(user.getUserEmails().size()).isEqualTo(1);

        EmailData email =  user.getUserEmails().iterator().next();
        assertThat(email).isNotNull();
        assertThat(email.getEmail()).isEqualTo("john@example.com");
        assertThat(email.getUser()).isSameAs(user);
    }
}