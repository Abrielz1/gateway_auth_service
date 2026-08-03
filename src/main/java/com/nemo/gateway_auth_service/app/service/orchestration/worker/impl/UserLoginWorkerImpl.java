package com.nemo.gateway_auth_service.app.service.orchestration.worker.impl;

import com.nemo.gateway_auth_service.app.domain.EmailData;
import com.nemo.gateway_auth_service.app.domain.PasswordData;
import com.nemo.gateway_auth_service.app.domain.User;
import com.nemo.gateway_auth_service.app.repository.UserRepository;
import com.nemo.gateway_auth_service.app.security.jwt.JwtUtils;
import com.nemo.gateway_auth_service.app.security.principal.AppUserDetails;
import com.nemo.gateway_auth_service.app.service.orchestration.worker.UserLoginWorker;
import com.nemo.gateway_auth_service.app.service.orchestration.worker.UserSessionWorker;
import com.nemo.gateway_auth_service.app.service.strategy.UserLookupStrategy;
import com.nemo.gateway_auth_service.app.service.strategy.impl.EmailStrategyImpl;
import com.nemo.gateway_auth_service.app.service.strategy.impl.LoginStrategyImpl;
import com.nemo.gateway_auth_service.app.service.strategy.impl.PhoneStrategyImpl;
import com.nemo.gateway_auth_service.web.model.request.UserLoginRequestDTO;
import com.nemo.gateway_auth_service.web.model.request.RefreshTokenRequestDto;
import com.nemo.gateway_auth_service.web.model.response.AuthTokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserLoginWorkerImpl implements UserLoginWorker {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final List<UserLookupStrategy> lookupStrategies;

    private final UserSessionWorker clientSessionWorker;

    private final JwtUtils jwtUtils;

    private static final String ROLES_CLAIM = "roles";

    @Override
    @Transactional(readOnly = true)
    public AuthTokenDto login(UserLoginRequestDTO loginRequest) {

        String id = loginRequest.clientIdentifier();

        UserLookupStrategy strategyCurrent = this.lookupStrategies.stream()
                .filter(strategy -> strategy.supports(id))
                .findFirst()
                .orElseThrow(() -> new BadCredentialsException("Invalid data"));

        Optional<User> client = Optional.empty();

      if (strategyCurrent instanceof EmailStrategyImpl) {
          client = this.userRepository.findByEmail(id);
      }

      if (strategyCurrent instanceof PhoneStrategyImpl) {
          client = this.userRepository.findByPhone(id);
      }

      if (strategyCurrent instanceof LoginStrategyImpl) {
          client = this.userRepository.findByLogin(id);
      }

        User clientFromDb = client.orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

      String passwordFromLogin = loginRequest.password();

      String passwordFromDB = clientFromDb.getUserPasswords().stream()
              .filter(PasswordData::getIsActive)
              .findFirst()
              .map(PasswordData::getPassword)
              .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        if (!this.passwordEncoder.matches(passwordFromLogin, passwordFromDB)) {
            log.error("someone tries to pickup a password");
            throw new BadCredentialsException("Invalid username or password");
        }

        var sessionId = UUID.randomUUID();
        var now = Instant.now();

        var authorities = clientFromDb.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .toList();

        var email = clientFromDb.getUserEmails().stream()
                .findFirst()
                .map(EmailData::getEmail).orElse("");

        AppUserDetails appUserDetails = new AppUserDetails(
                clientFromDb.getUserUUID(),
                email,
                authorities,
                passwordFromDB,
                null,
                Boolean.TRUE.equals(clientFromDb.getEnabled()),
                !Boolean.TRUE.equals(clientFromDb.getIsDeleted())

        );

        var accessToken = this.jwtUtils.generateAccessToken(appUserDetails, sessionId, now);
    var refreshToken = this.jwtUtils.generateRefreshToken(appUserDetails, sessionId, now);

    this.clientSessionWorker.saveKeysToRedis(clientFromDb.getUserUUID(), accessToken, refreshToken);

      return new AuthTokenDto(accessToken, refreshToken, clientFromDb.getUserUUID().toString());
    }

    @Override
    public AuthTokenDto refresh(RefreshTokenRequestDto refreshTokenRequestDto) {
        var claims = this.jwtUtils.getAllClaimsFromToken (refreshTokenRequestDto.refreshToken());
        var tokenType = claims.get("token_type", String.class);

        if (!StringUtils.hasText(tokenType) ||!Objects.equals("Refresh", tokenType)) {
            log.error("Attempt to refresh with invalid token type: {}", tokenType);
            throw new BadCredentialsException("Invalid token type");
        }

        var userId = UUID.fromString(claims.getSubject());
        var sessionId = UUID.fromString(claims.getId());
        var tokenFromRedis = this.clientSessionWorker.getRefreshTokenFromRedis(userId);
        String email = claims.get("email", String.class);

        User clientFromDb = this.userRepository.findByUuid(userId)
                .orElseThrow(() -> new BadCredentialsException("User not found"));

        List<SimpleGrantedAuthority> authorities = clientFromDb.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .toList();

        if (!StringUtils.hasText(tokenFromRedis)) {
            log.error("Attempt to refresh with invalid token: {}", tokenFromRedis);
            throw new BadCredentialsException("Invalid token type");
        }

        if (!Objects.equals(tokenFromRedis, refreshTokenRequestDto.refreshToken())) {
            this.clientSessionWorker.deleteSession(userId);
            log.error("Token hijack attempt detected for user: {}!", userId);
            throw new BadCredentialsException("Session Expired");
        }

        AppUserDetails appUserDetails = new AppUserDetails(
                userId,
                email,
                authorities,
                "",
                null,
                Boolean.TRUE.equals(clientFromDb.getEnabled()),
                !Boolean.TRUE.equals(clientFromDb.getIsDeleted())
        );

        var now = Instant.now();
        var newAccessToken = this.jwtUtils.generateAccessToken(appUserDetails, sessionId, now);
        var newRefreshToken = this.jwtUtils.generateRefreshToken(appUserDetails, sessionId, now);

        this.clientSessionWorker.saveKeysToRedis(userId, newAccessToken, newRefreshToken);

        return new AuthTokenDto(newAccessToken, newRefreshToken, userId.toString());
    }
}
