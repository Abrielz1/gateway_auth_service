package com.nemo.gateway_auth_service.app.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nemo.gateway_auth_service.app.domain.dto.redis.AccessTokenKey;
import com.nemo.gateway_auth_service.app.domain.dto.redis.RefreshTokenKey;
import com.nemo.gateway_auth_service.app.repository.redis.dao.RedisRepositoryDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories()
public class RedisConfiguration {

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }

    @Bean
    public RedisRepositoryDao<AccessTokenKey, String> accessTokenRedisRepository(
            StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {

        return new RedisRepositoryDao<>(redisTemplate, objectMapper, String.class);
    }

    @Bean
    public RedisRepositoryDao<RefreshTokenKey, String> refreshTokenRedisRepository(
            StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {

        return new RedisRepositoryDao(redisTemplate, objectMapper, String.class);
    }
}
