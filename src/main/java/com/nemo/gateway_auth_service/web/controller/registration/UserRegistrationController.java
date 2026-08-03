package com.nemo.gateway_auth_service.web.controller.registration;

import com.nemo.gateway_auth_service.app.service.orchestration.facade.UserRegistrationFacade;
import com.nemo.gateway_auth_service.web.model.request.UserRegistrationRequestDTO;
import com.nemo.gateway_auth_service.web.model.response.UserRegistrationResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "Client oriented controller", description = "Clients management")
@Validated
@RestController
@RequestMapping("/api/v1/registration/client")
@RequiredArgsConstructor
public class UserRegistrationController {

   private final UserRegistrationFacade clientRegistrationFacade;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserRegistrationResponseDto registerNewClient(@Valid @RequestBody UserRegistrationRequestDTO request) {

        log.info("Client registration attempt: {}", request.email());
        return this.clientRegistrationFacade.register(request);
    }
   // todo сделать подтверждение по почте и пре регистрацию
}
