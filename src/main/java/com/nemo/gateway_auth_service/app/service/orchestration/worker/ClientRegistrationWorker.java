package com.nemo.gateway_auth_service.app.service.orchestration.worker;

import com.nemo.gateway_auth_service.web.model.request.ClientRegistrationRequestDTO;
import com.nemo.gateway_auth_service.web.model.response.ClientRegistrationResponseDto;

public interface ClientRegistrationWorker {

    ClientRegistrationResponseDto register(ClientRegistrationRequestDTO clientRegisterRequestDTO);
}
