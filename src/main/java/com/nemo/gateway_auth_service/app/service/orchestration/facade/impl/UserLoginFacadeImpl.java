package com.nemo.gateway_auth_service.app.service.orchestration.facade.impl;

import com.nemo.gateway_auth_service.app.service.orchestration.facade.UserLoginFacade;
import com.nemo.gateway_auth_service.app.service.orchestration.worker.UserLogOutWorker;
import com.nemo.gateway_auth_service.app.service.orchestration.worker.UserLoginWorker;
import com.nemo.gateway_auth_service.web.model.request.UserLoginRequestDTO;
import com.nemo.gateway_auth_service.web.model.request.UserLogoutRequestDto;
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
public class UserLoginFacadeImpl implements UserLoginFacade {

    private final UserLoginWorker clientLoginWorker;

    private final UserLogOutWorker clientLogOutWorker;

    @Override
    public AuthTokenDto login(UserLoginRequestDTO loginRequest) {

        return this.clientLoginWorker.login(loginRequest);
    }

    @Override
    //@PreAuthorize("")
    public AuthTokenDto refresh(RefreshTokenRequestDto refreshToken) {

        return this.clientLoginWorker.refresh(refreshToken);
    }

    @Override
    public void logout(@Valid UserLogoutRequestDto requestDto) {

        this.clientLogOutWorker.logout(requestDto);
    }
}
