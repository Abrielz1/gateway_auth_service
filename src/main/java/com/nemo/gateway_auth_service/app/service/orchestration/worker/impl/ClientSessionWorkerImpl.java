package com.nemo.gateway_auth_service.app.service.orchestration.worker.impl;

import com.nemo.gateway_auth_service.app.domain.dto.JwtProperties;
import com.nemo.gateway_auth_service.app.domain.dto.redis.AccessTokenKey;
import com.nemo.gateway_auth_service.app.domain.dto.redis.RefreshTokenKey;
import com.nemo.gateway_auth_service.app.repository.redis.dao.RedisRepositoryDao;
import com.nemo.gateway_auth_service.app.service.orchestration.worker.ClientSessionWorker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClientSessionWorkerImpl implements ClientSessionWorker {

    private final RedisRepositoryDao<AccessTokenKey, String> accessTokenKeyRedisRepository;

    private final RedisRepositoryDao<RefreshTokenKey, String> refreshTokenKeyRedisRepository;

    private final JwtProperties jwtProps;

    public void saveKeysToRedis(UUID userUUID, String accessTokenKey, String refreshTokenKey) {

        this.accessTokenKeyRedisRepository.save(AccessTokenKey.of(userUUID), accessTokenKey, this.jwtProps.expiration().access());
        this.refreshTokenKeyRedisRepository.save(RefreshTokenKey.of(userUUID), refreshTokenKey, this.jwtProps.expiration().refresh());
    }

    @Override
    public String getRefreshTokenFromRedis(UUID userId) {
        return this.refreshTokenKeyRedisRepository.findByKeyOrDefault(RefreshTokenKey.of(userId), "");
    }

    @Override
    public void deleteSession(UUID userId) {
        this.accessTokenKeyRedisRepository.delete(AccessTokenKey.of(userId));
        this.refreshTokenKeyRedisRepository.delete(RefreshTokenKey.of(userId));
    }
}
