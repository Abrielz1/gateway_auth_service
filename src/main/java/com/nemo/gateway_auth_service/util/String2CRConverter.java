package com.nemo.gateway_auth_service.util;

import com.nemo.gateway_auth_service.app.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class String2CRConverter {

    private final JwtUtils jwtUtils;

    public String convertIntoCRC(String teargetString) {

        try {

            // return this.jwtUtils.createFingerprintHash(teargetString);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    private String convertByteArrayToHexString(byte[] arrayBytes) {

        var stringBuffer = new StringBuilder();

        for (byte arrayByte : arrayBytes) {
            stringBuffer.append(Integer.toString((arrayByte & 0xff) + 0x100, 16)
                    .substring(1));
        }

        return stringBuffer.toString();
    }
}
