package com.nemo.gateway_auth_service.app.security.jwt;

import com.nemo.gateway_auth_service.app.domain.dto.JwtProperties;
import com.nemo.gateway_auth_service.app.security.principal.AppUserDetails;
import com.nemo.gateway_auth_service.util.IdGenerationServiceImpl;
import com.nemo.gateway_auth_service.util.exception.exceptions.InvalidJwtAuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtils {

    private final String ROLES = "roles";

   @Value("${app.jwt.secret}")
    private final JwtProperties rawSecret;

    private final JwtProperties jwtProps;

    private SecretKey secretKey;

 //   private final IdGenerationServiceImpl idGenerationService;

    @PostConstruct
    public void init() {

        if (rawSecret == null || rawSecret.secret().isBlank()) {
            throw new IllegalArgumentException("JWT secret is not configured in application properties (app.jwt.secret)");
        }
        try {
            this.secretKey = new SecretKeySpec(Base64.getDecoder().decode(rawSecret.secret()), "HmacSHA512");
            log.info("JWT secret key initialized successfully.");
        } catch (Exception e) {
            log.error("Invalid JWT secret key. It must be a Base64-encoded string.", e);
            throw new IllegalArgumentException("Invalid JWT secret key.", e);
        }
    }

    public String generateAccessToken(AppUserDetails userDetails, UUID sessionId, Instant now) {

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts.builder()
                .subject(String.valueOf(userDetails.getId()))
                .issuer(userDetails.getUsername())
                .claim("userId", userDetails.getId())
                .claim(ROLES, roles)
                .claim("email", userDetails.getEmail())
                .id(sessionId.toString())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(this.jwtProps.expiration().access())))
                .signWith(secretKey)
                .compact();
    }

    public String generateRefreshToken(AppUserDetails userDetails, UUID sessionId, Instant now) {

       // String refreshTokenUniqueUUId = this.idGenerationService.generateUniqueTokenId();
        return Jwts.builder()
                .subject(String.valueOf(userDetails.getId()))
                .issuer(userDetails.getUsername())
               // .id(refreshTokenUniqueUUId)
                .id(UUID.randomUUID().toString())
                .claim("sessionId", sessionId.toString())
                .claim("email", userDetails.getEmail())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(this.jwtProps.expiration().refresh())))
                .signWith(secretKey)
                .compact();
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return true;
        } catch (SignatureException e) {
            log.warn("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.warn("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.debug("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.warn("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.warn("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    public Claims getAllClaimsFromToken(String token) {
        try {
            return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Error parsing JWT claims", e);
            throw new InvalidJwtAuthenticationException("Failed to parse JWT claims");
        }
    }

    public UUID getSessionId(Claims claims) {
        return UUID.fromString(claims.getId());
    }

    public UUID getUserId(Claims claims) {
        String userId = claims.getSubject();

        return StringUtils.hasText(userId) ? UUID.fromString(userId) : null;
    }

    public String getEmail(Claims claims) {
        return claims.getSubject();
    }

    public Instant getExpiration(Claims claims) {
        return claims.getExpiration().toInstant();
    }

    @SuppressWarnings("unchecked")
    public List<SimpleGrantedAuthority> getRoleClaims(Claims claims) {
        List<String> roles = claims.get("roles", List.class);
        if (roles == null || roles.isEmpty()) {
            return Collections.emptyList();
        }
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    public List<? extends GrantedAuthority> getAuthorities(Claims claims) {
        List<String> roles = claims.get(ROLES, List.class);
        if (roles == null || roles.isEmpty()) {
            return Collections.emptyList();
        }
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }
}

