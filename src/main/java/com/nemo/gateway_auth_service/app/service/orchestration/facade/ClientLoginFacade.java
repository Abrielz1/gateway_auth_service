package com.nemo.gateway_auth_service.app.service.orchestration.facade;

import com.nemo.gateway_auth_service.web.model.request.ClientLoginRequestDTO;
import com.nemo.gateway_auth_service.web.model.request.ClientLogoutRequestDto;
import com.nemo.gateway_auth_service.web.model.request.RefreshTokenRequestDto;
import com.nemo.gateway_auth_service.web.model.response.AuthTokenDto;

public interface ClientLoginFacade {

    AuthTokenDto login(ClientLoginRequestDTO loginRequest);

    AuthTokenDto refresh(RefreshTokenRequestDto refreshToken);

    void logout(ClientLogoutRequestDto requestDto);
}
