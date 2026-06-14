package com.nemo.gateway_auth_service.app.service.orchestration.worker;

import com.nemo.gateway_auth_service.web.model.request.ClientLogoutRequestDto;

public interface ClientLogOutWorker {

   void logout(ClientLogoutRequestDto requestDto);
}
