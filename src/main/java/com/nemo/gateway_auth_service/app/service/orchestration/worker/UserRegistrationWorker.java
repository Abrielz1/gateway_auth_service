package com.nemo.gateway_auth_service.app.service.orchestration.worker;

import com.nemo.gateway_auth_service.web.model.request.UserRegistrationRequestDTO;
import com.nemo.gateway_auth_service.web.model.response.UserRegistrationResponseDto;

public interface UserRegistrationWorker {

    UserRegistrationResponseDto register(UserRegistrationRequestDTO clientRegisterRequestDTO);
}
