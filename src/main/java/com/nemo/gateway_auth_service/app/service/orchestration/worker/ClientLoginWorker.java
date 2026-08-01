package com.nemo.gateway_auth_service.app.service.orchestration.worker;

import com.nemo.gateway_auth_service.web.model.request.ClientLoginRequestDTO;
import com.nemo.gateway_auth_service.web.model.request.RefreshTokenRequestDto;
import com.nemo.gateway_auth_service.web.model.response.AuthTokenDto;

public interface ClientLoginWorker {

    AuthTokenDto login(ClientLoginRequestDTO loginRequest);

    AuthTokenDto refresh(RefreshTokenRequestDto refreshTokenRequestDto);
}
