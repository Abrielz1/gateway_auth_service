package com.nemo.gateway_auth_service.app.service.orchestration.facade.impl;

import com.nemo.gateway_auth_service.app.service.orchestration.facade.ClientLoginFacade;
import com.nemo.gateway_auth_service.app.service.orchestration.worker.ClientLogOutWorker;
import com.nemo.gateway_auth_service.app.service.orchestration.worker.ClientLoginWorker;
import com.nemo.gateway_auth_service.web.model.request.ClientLoginRequestDTO;
import com.nemo.gateway_auth_service.web.model.request.ClientLogoutRequestDto;
import com.nemo.gateway_auth_service.web.model.request.RefreshTokenRequestDto;
import com.nemo.gateway_auth_service.web.model.response.AuthTokenDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class ClientLoginFacadeImpl implements ClientLoginFacade {

    private final ClientLoginWorker clientLoginWorker;

    private final ClientLogOutWorker clientLogOutWorker;

    @Override
    public AuthTokenDto login(ClientLoginRequestDTO loginRequest) {

        return this.clientLoginWorker.login(loginRequest);
    }

    @Override
    public AuthTokenDto refresh(String refreshToken) {

        return this.clientLoginWorker.refresh(new RefreshTokenRequestDto(refreshToken));
    }

    @Override
    public void logout(@Valid ClientLogoutRequestDto requestDto) {

        this.clientLogOutWorker.logout(requestDto);
    }
}
