package com.nemo.gateway_auth_service.app.service.orchestration.facade.impl;

import com.nemo.gateway_auth_service.app.service.orchestration.facade.UserRegistrationFacade;
import com.nemo.gateway_auth_service.app.service.orchestration.worker.UserRegistrationWorker;
import com.nemo.gateway_auth_service.web.model.request.UserRegistrationRequestDTO;
import com.nemo.gateway_auth_service.web.model.response.UserRegistrationResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class UserRegistrationFacadeImpl implements UserRegistrationFacade {

    private final UserRegistrationWorker clientRegistrationWorker;


    @Override
    public UserRegistrationResponseDto register(UserRegistrationRequestDTO clientRegisterRequestDTO) {

        log.info("Facade routing registration request for user: {}", clientRegisterRequestDTO.username());

        return this.clientRegistrationWorker.register(clientRegisterRequestDTO);
    }
}
