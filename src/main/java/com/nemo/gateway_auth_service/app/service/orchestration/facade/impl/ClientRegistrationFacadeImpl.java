package com.nemo.gateway_auth_service.app.service.orchestration.facade.impl;

import com.nemo.gateway_auth_service.app.service.orchestration.facade.ClientRegistrationFacade;
import com.nemo.gateway_auth_service.app.service.orchestration.worker.ClientRegistrationWorker;
import com.nemo.gateway_auth_service.web.model.request.ClientRegistrationRequestDTO;
import com.nemo.gateway_auth_service.web.model.response.ClientRegistrationResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class ClientRegistrationFacadeImpl implements ClientRegistrationFacade {

    private final ClientRegistrationWorker clientRegistrationWorker;


    @Override
    public ClientRegistrationResponseDto register(ClientRegistrationRequestDTO clientRegisterRequestDTO) {

        log.info("Facade routing registration request for user: {}", clientRegisterRequestDTO.username());

        return this.clientRegistrationWorker.register(clientRegisterRequestDTO);
    }
}
