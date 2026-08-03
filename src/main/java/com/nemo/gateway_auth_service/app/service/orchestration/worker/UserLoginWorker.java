package com.nemo.gateway_auth_service.app.service.orchestration.worker;

import com.nemo.gateway_auth_service.web.model.request.UserLoginRequestDTO;
import com.nemo.gateway_auth_service.web.model.request.RefreshTokenRequestDto;
import com.nemo.gateway_auth_service.web.model.response.AuthTokenDto;

public interface UserLoginWorker {

    AuthTokenDto login(UserLoginRequestDTO loginRequest);

    AuthTokenDto refresh(RefreshTokenRequestDto refreshTokenRequestDto);
}
