package com.nemo.gateway_auth_service.app.repository.redis;

import com.nemo.gateway_auth_service.app.domain.dto.redis.AccessTokenKey;
import com.nemo.gateway_auth_service.app.domain.dto.redis.RefreshTokenKey;
import com.nemo.gateway_auth_service.app.repository.redis.dao.RedisRepositoryDao;
import com.nemo.gateway_auth_service.util.exception.exceptions.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import java.time.Duration;
import java.util.UUID;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RedisTokenRepositoryImpl implements RedisTokenRepository {

    private final RedisRepositoryDao<AccessTokenKey, String> accessRepo;
    private final RedisRepositoryDao<RefreshTokenKey, String> refreshRepo;

    public String getAccessTokenKey(UUID userId) {
        return this.accessRepo.findByKey(AccessTokenKey.of(userId)).orElseThrow( () ->{
             log.warn("[WARN] accessTokenKey not found for userId {}", userId);
             return new ObjectNotFoundException("bad username or password");
            }
        );
    }

    public String getRefreshTokenKey(UUID userId) {
        return this.refreshRepo.findByKey(RefreshTokenKey.of(userId)).orElseThrow(() -> {
            log.warn("[WARN] refreshTokenKey not found for userId {}", userId);
            return new ObjectNotFoundException("bad username or password");
            }
        );
    }

    @Override
    public void saveTokens(UUID userId, String accessToken, String refreshToken) {

        this.accessRepo.save(AccessTokenKey.of(userId), accessToken, Duration.ofMinutes(5L));
        this.refreshRepo.save(RefreshTokenKey.of(userId), refreshToken, Duration.ofDays(180));
    }

    @Override
    public void logout(UUID userId) {
        this.refreshRepo.delete(RefreshTokenKey.of(userId));
        this.accessRepo.delete(AccessTokenKey.of(userId));
    }
}
