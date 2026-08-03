package com.nemo.gateway_auth_service.app.service.orchestration.worker.impl;

import com.nemo.gateway_auth_service.app.security.jwt.JwtUtils;
import com.nemo.gateway_auth_service.app.service.orchestration.worker.UserLogOutWorker;
import com.nemo.gateway_auth_service.app.service.orchestration.worker.UserSessionWorker;
import com.nemo.gateway_auth_service.web.model.request.UserLogoutRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor()
public class UserLogOutWorkerImpl implements UserLogOutWorker {

    private final UserSessionWorker clientSessionWorker;

    private final JwtUtils jwtUtils;

    @Override
    public void logout(@Valid UserLogoutRequestDto requestDto) {
    var claims = this.jwtUtils.getAllClaimsFromToken(requestDto.refreshToken());
        var userId = UUID.fromString(claims.getSubject());
        this.clientSessionWorker.deleteSession(userId);
    }
}
