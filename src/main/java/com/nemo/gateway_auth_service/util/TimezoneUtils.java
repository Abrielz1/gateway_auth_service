package com.nemo.gateway_auth_service.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.time.ZoneId;
import java.util.TimeZone;

@Slf4j
@Component
public class TimezoneUtils {

    /**
     * Пытается определить таймзону клиента.
     * В идеале, фронтенд должен присылать ее в заголовке X-Time-Zone.
     * Если заголовка нет - фоллбэк на дефолтную таймзону сервера.
     */
    public ZoneId getZoneIdFromRequest(HttpServletRequest request) {
        String timeZoneHeader = request.getHeader("X-Time-Zone");
        try {
            if (timeZoneHeader != null) {
                return ZoneId.of(timeZoneHeader);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return TimeZone.getDefault().toZoneId();
    }
}
