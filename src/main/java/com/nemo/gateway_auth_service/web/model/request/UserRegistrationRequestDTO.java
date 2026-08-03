package com.nemo.gateway_auth_service.web.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UserRegistrationRequestDTO(
        @NotBlank(message = "Имя пользователя не может быть пустым")
        @Size(min = 8, max = 256, message = "Имя пользователя должено быть от 8 до 256 символов")
        String username,

        @NotBlank(message = "Email не может быть пустым")
        @Email(message = "Неверный формат email")
        String email,

        @NotBlank(message = "Пароль обязателен")
        @Size(min = 8, max = 64, message = "Пароль должен быть от 8 до 64 символов")
        String password,

        @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Неверный формат телефона")
        String phone) {
}