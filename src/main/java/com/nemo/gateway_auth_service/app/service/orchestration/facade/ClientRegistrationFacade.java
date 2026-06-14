package com.nemo.gateway_auth_service.app.service.orchestration.facade;

import com.nemo.gateway_auth_service.web.model.request.ClientRegistrationRequestDTO;
import com.nemo.gateway_auth_service.web.model.response.ClientRegistrationResponseDto;

public interface ClientRegistrationFacade {

    ClientRegistrationResponseDto register(ClientRegistrationRequestDTO clientRegisterRequestDTO);
}
