package com.nemo.gateway_auth_service.web.controller.secutiry;

import com.nemo.gateway_auth_service.app.service.orchestration.facade.ClientLoginFacade;
import com.nemo.gateway_auth_service.web.model.request.ClientLoginRequestDTO;
import com.nemo.gateway_auth_service.web.model.request.ClientLogoutRequestDto;
import com.nemo.gateway_auth_service.web.model.request.RefreshTokenRequestDto;
import com.nemo.gateway_auth_service.web.model.response.AuthTokenDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "Authentication", description = "User authentication and token management")
@Validated
@RequestMapping("/api/v1/auth/user")
@RestController
@RequiredArgsConstructor
public class AuthClientController {

    private final ClientLoginFacade clientLoginFacade;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public AuthTokenDto login(@Valid @RequestBody ClientLoginRequestDTO loginRequestDTO) {

        return this.clientLoginFacade.login(loginRequestDTO);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/refresh")
    public AuthTokenDto refresh(@Valid @RequestBody RefreshTokenRequestDto refreshTokenRequestDto) {

        return this.clientLoginFacade.refresh(refreshTokenRequestDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/logout")
    public void logout(@Valid @RequestBody ClientLogoutRequestDto clientLogoutRequestDTO) {

        this.clientLoginFacade.logout(clientLogoutRequestDTO);
    }
}
