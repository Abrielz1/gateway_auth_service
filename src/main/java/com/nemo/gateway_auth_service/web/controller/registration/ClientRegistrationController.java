package com.nemo.gateway_auth_service.web.controller.registration;

import com.nemo.gateway_auth_service.app.service.orchestration.facade.ClientRegistrationFacade;
import com.nemo.gateway_auth_service.web.model.request.ClientRegistrationRequestDTO;
import com.nemo.gateway_auth_service.web.model.response.ClientRegistrationResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "Client oriented controller", description = "Clients management")
@Validated
@RestController
@RequestMapping("/api/v1/registaration/cient")
@RequiredArgsConstructor
public class ClientRegistrationController {

   private final ClientRegistrationFacade clientRegistrationFacade;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ClientRegistrationResponseDto registerNewClient(ClientRegistrationRequestDTO request) {

        log.info("Client registration attempt: {}", request.email());
        return this.clientRegistrationFacade.register(request);
    }
   // todo сделать подтверждение по почте и пре регистрацию
}
