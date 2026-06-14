package com.nemo.gateway_auth_service.app.security.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nemo.gateway_auth_service.app.domain.entity.enums.RoleType;
import com.nemo.gateway_auth_service.app.security.handler.AccessDeniedHandlerImpl;
import com.nemo.gateway_auth_service.app.security.jwt.JwtAuthenticationEntryPoint;
import com.nemo.gateway_auth_service.app.security.jwt.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtTokenFilter jwtTokenFilter;
    private final ObjectMapper objectMapper;

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new AccessDeniedHandlerImpl(objectMapper);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public BCryptPasswordEncoder create() {
        return new BCryptPasswordEncoder(16);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:8080"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Fingerprint"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler())
                )
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/api/v1/auth/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**"
                        ).permitAll()

                        .requestMatchers("/api/v1/security/officer/**").hasAnyAuthority(

                                RoleType.ROLE_ADMIN.name() // Админ может все
                        )
                        .requestMatchers("/api/v1/security/supervisor/**").hasAnyAuthority(

                                RoleType.ROLE_ADMIN.name()
                        )

                        .requestMatchers("/api/v1/management/manager/**").hasAnyAuthority(

                                RoleType.ROLE_ADMIN.name()
                        )
                        .requestMatchers("/api/v1/management/top/**").hasAnyAuthority(

                                RoleType.ROLE_ADMIN.name()
                        )

                        .requestMatchers("/api/v1/admin/**").hasAuthority(RoleType.ROLE_ADMIN.name())

                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
