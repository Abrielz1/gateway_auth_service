package com.nemo.gateway_auth_service.app.service.orchestration.worker.impl;

import com.nemo.gateway_auth_service.app.service.orchestration.worker.ClientLogOutWorker;
import com.nemo.gateway_auth_service.app.service.orchestration.worker.ClientSessionWorker;
import com.nemo.gateway_auth_service.web.model.request.ClientLogoutRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor()
public class ClientLogOutWorkerImpl implements ClientLogOutWorker {

    private final ClientSessionWorker clientSessionWorker;

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void logout(@Valid ClientLogoutRequestDto requestDto) {
    var claims = this.jwtTokenProvider.getClaimsFromToken(requestDto.refreshToken());
        var userId = UUID.fromString(claims.getSubject());
        this.clientSessionWorker.deleteSession(userId);
    }
}
