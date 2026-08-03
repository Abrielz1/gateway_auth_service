package com.nemo.gateway_auth_service.app.service.orchestration.facade;

import com.nemo.gateway_auth_service.web.model.request.UserLoginRequestDTO;
import com.nemo.gateway_auth_service.web.model.request.UserLogoutRequestDto;
import com.nemo.gateway_auth_service.web.model.request.RefreshTokenRequestDto;
import com.nemo.gateway_auth_service.web.model.response.AuthTokenDto;

public interface UserLoginFacade {

    AuthTokenDto login(UserLoginRequestDTO loginRequest);

    AuthTokenDto refresh(RefreshTokenRequestDto refreshToken);

    void logout(UserLogoutRequestDto requestDto);
}
