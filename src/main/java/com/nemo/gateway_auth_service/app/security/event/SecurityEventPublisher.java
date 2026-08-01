package com.nemo.gateway_auth_service.app.security.event;

import com.nemo.gateway_auth_service.app.security.entity.SecurityEvent;
import com.nemo.gateway_auth_service.app.security.entity.enums.EventType;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SecurityEventPublisher {


    private final ApplicationEventPublisher publisher;

    public void publish(EventType type, Long userId, UUID sessionId, HttpServletRequest request) {
        publish(type, userId, sessionId, request, Collections.emptyMap());
    }

    public void publish(EventType type, Long userId, UUID sessionId, HttpServletRequest request, Map<String, ? extends Serializable> details) {
        String ip = (request != null) ? request.getRemoteAddr() : "N/A";
        String ua = (request != null) ? request.getHeader("User-Agent") : "N/A";
        publisher.publishEvent(new SecurityEvent(type, userId, sessionId, ip, ua, details));
    }
}
