package com.nemo.gateway_auth_service.util;

import com.nemo.gateway_auth_service.util.exception.exceptions.EncryptionDataFailureException;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Base64;

@Slf4j
@Component
@Converter(autoApply = false)
@RequiredArgsConstructor
public class AesCryptoConverter implements AttributeConverter<String, String> {

    @Value("${app.jwt.encryption.secret-key}")
    private String secretKeyString;
    private SecretKeySpec secretKey;
    private final SecureRandom secureRandom = new SecureRandom();

    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int IV_LENGTH_BYTES = 12;
    private static final int TAG_LENGTH_BITS = 128;

    @PostConstruct
    public void init() {

        if (secretKeyString == null || secretKeyString.isBlank()) {
            throw new IllegalStateException("Encryption secret key is not configured!");
        }

        byte[] decodedKey;
        try {

            decodedKey = Base64.getDecoder().decode(secretKeyString);

            if (decodedKey.length != 16 && decodedKey.length != 24 && decodedKey.length != 32) {
                throw new IllegalStateException(
                        "AES key length must be 16, 24, or 32 bytes, but got " + decodedKey.length
                );
            }

            this.secretKey = new SecretKeySpec(decodedKey, "AES");
            log.info("AES Crypto Converter initialized successfully.");
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Invalid Base64 for encryption key", e);
        }
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) return null;
        try {
            byte[] iv = new byte[IV_LENGTH_BYTES];
            secureRandom.nextBytes(iv);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(TAG_LENGTH_BITS, iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);

            byte[] cipherText = cipher.doFinal(attribute.getBytes(StandardCharsets.UTF_8));

            ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + cipherText.length);
            byteBuffer.put(iv);
            byteBuffer.put(cipherText);

            return Base64.getEncoder().encodeToString(byteBuffer.array());
        } catch (GeneralSecurityException e) {
            log.error("FATAL: Could not encrypt data", e);
            throw new EncryptionDataFailureException("Encryption failed");
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(dbData);
            ByteBuffer byteBuffer = ByteBuffer.wrap(decodedBytes);

            byte[] iv = new byte[IV_LENGTH_BYTES];
            byteBuffer.get(iv);
            byte[] cipherText = new byte[byteBuffer.remaining()];
            byteBuffer.get(cipherText);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(TAG_LENGTH_BITS, iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);

            byte[] decryptedBytes = cipher.doFinal(cipherText);

            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (GeneralSecurityException e) {
            log.error("FATAL: Could not decrypt data. Data might be corrupted or key has changed.", e);
            throw new IllegalStateException("Failed to decrypt database column value", e);
        }
    }
}