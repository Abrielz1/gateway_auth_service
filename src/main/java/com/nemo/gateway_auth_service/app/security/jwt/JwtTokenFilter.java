package com.nemo.gateway_auth_service.app.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nemo.gateway_auth_service.app.repository.UserRepository;
import com.nemo.gateway_auth_service.app.security.principal.AppUserDetails;
import com.nemo.gateway_auth_service.app.security.service.RateLimiterService;
import com.nemo.gateway_auth_service.util.exception.exceptions.InvalidJwtAuthenticationException;
import com.nemo.gateway_auth_service.util.exception.exceptions.RateLimitExceededException;
import com.nemo.gateway_auth_service.util.exception.exceptions.SecurityBreachAttemptException;
import io.github.bucket4j.Bucket;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    private final RateLimiterService rateLimiterService;

    private final ObjectMapper objectMapper;

    private final UserRepository userRepository;

// todo для v1 перекинуть код с authservice

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws IOException {

        try {
            final String token = extractTokenFromRequest(request);
            if (token == null) {
                log.trace("emty token wasgiven into doFilterInternal");
                filterChain.doFilter(request, response);
                return;
            }

            if (!jwtUtils.isTokenValid(token)) {

                var ip = request.getRemoteAddr();
                Bucket bucket = this.rateLimiterService.resolveBucket(ip);

                if (!bucket.tryConsume(1)) {
                    log.warn("BRUTE-FORCE ALERT! IP {} exceeded token validation limits.", ip);
                    response.sendError(429, "Too Many Requests. You are temporarily blocked.");
                    throw new RateLimitExceededException("Too Many Requests. You are temporarily blocked.");
                }

                this.applySecurityDelay();
                throw new InvalidJwtAuthenticationException("Token is expired or has invalid signature");
            }

            final Claims claims = jwtUtils.getAllClaimsFromToken(token);
            var userId = UUID.fromString(claims.getSubject());

            var userFromDB = this.userRepository.findByUserUUID(userId);
            var isEnabled = false;
            var accountNonLocked = false;
            if (userFromDB.isPresent()) {
                isEnabled = Boolean.TRUE.equals(userFromDB.get().getEnabled());
                accountNonLocked = !Boolean.TRUE.equals(userFromDB.get().getIsDeleted());
            } else {
                throw new InvalidJwtAuthenticationException("User not found");
            }

            AppUserDetails userDetails = new AppUserDetails(
                    userId,
                    this.jwtUtils.getEmail(claims),
                    this.jwtUtils.getAuthorities(claims),
                    "",
                    jwtUtils.getExpiration(claims),
                    isEnabled,
                    accountNonLocked
            );

            this.setAuthentication(request, userDetails);
            filterChain.doFilter(request, response);

        } catch (Exception ex) {
            SecurityContextHolder.clearContext();
            this.handleAuthError(response, request, ex);
        }
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    private void setAuthentication(HttpServletRequest request, UserDetails userDetails) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void handleAuthError(HttpServletResponse response, HttpServletRequest request, Exception e) throws IOException {
        final String errorId = "AUTH-ERR-" + System.currentTimeMillis();
        log.error("Authentication error [{}]: {}", errorId, e.getMessage(), e);
        response.setHeader("X-Error-ID", errorId);

        int status = 0;
        String errorName = null;

        if (e instanceof RateLimitExceededException) {
            status = 429;
            errorName = "Too Many Requests";
        } else if (e instanceof SecurityException || e instanceof SecurityBreachAttemptException) {
            status = HttpServletResponse.SC_FORBIDDEN;
            errorName = "Forbidden";
        } else {
            status = HttpServletResponse.SC_UNAUTHORIZED;
            errorName = "Unauthorized";
        }

        response.setContentType("application/json");
        response.setStatus(status);

        final java.util.Map<String, Object> body = new java.util.HashMap<>();
        body.put("status", status);
        body.put("error", errorName);
        body.put("message", e.getMessage());
        body.put("path", request.getServletPath());
        body.put("ref", errorId);

        objectMapper.writeValue(response.getOutputStream(), body);
    }

    private void applySecurityDelay() {
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(100, 200));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}