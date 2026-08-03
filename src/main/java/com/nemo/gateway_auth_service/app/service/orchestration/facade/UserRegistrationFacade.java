package com.nemo.gateway_auth_service.app.service.orchestration.facade;

import com.nemo.gateway_auth_service.web.model.request.UserRegistrationRequestDTO;
import com.nemo.gateway_auth_service.web.model.response.UserRegistrationResponseDto;

public interface UserRegistrationFacade {

    UserRegistrationResponseDto register(UserRegistrationRequestDTO clientRegisterRequestDTO);
}
