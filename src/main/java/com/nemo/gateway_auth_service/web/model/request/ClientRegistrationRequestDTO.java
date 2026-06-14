package com.nemo.gateway_auth_service.web.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Builder
public record ClientRegistrationRequestDTO(
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
        String phone,

        @NotBlank(message = "Дата рождения не может быть пустой")
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$|^$", message = "Дата рождения должна быть в формате YYYY-MM-DD")
        String dateOfBirth) {

    public ClientRegistrationRequestDTO {

        LocalDate date;
        try {

            date = LocalDate.parse(dateOfBirth, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Неверный формат даты рождения. Ожидается YYYY-MM-DD", e);
        }

        if (date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Дата рождения не может быть в будущем");
        }

        if (!date.isBefore(LocalDate.now().minusYears(18))) {
            throw new IllegalArgumentException("Доступ разрешён только пользователям старше 18 лет");
        }
    }
}