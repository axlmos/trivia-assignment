package trivia.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import trivia.exception.InvalidTokenException;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class JweTokenService {

    @Value("${jwt.secret}")
    private final byte[] secretKey;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Constructor of the JweTokenService
     *
     * @param secret The secret key to encrypt and decrypt with
     */
    public JweTokenService(@Value("${jwt.secret}") String secret) {
        this.secretKey = secret.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Encrypts the correct answers using AES-256-GCM
     *
     * @param correctAnswers The answers to encrypt
     * @return An encrypted token of the answers
     */
    public String encryptAnswers(List<String> correctAnswers) {
        try {
            String json = objectMapper.writeValueAsString(correctAnswers);

            JWEHeader header = new JWEHeader(JWEAlgorithm.DIR, EncryptionMethod.A256GCM);
            Payload payload = new Payload(json);
            JWEObject jweObject = new JWEObject(header, payload);

            jweObject.encrypt(new DirectEncrypter(secretKey));

            return jweObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException("Failed to encrypt answers", e);
        }
    }

    /**
     * Decrypts the correct answers
     *
     * @param token The token to decrypt
     * @return The decrypted correct answers
     */
    public List<String> decryptAnswers(String token) {
        try {
            JWEObject jweObject = JWEObject.parse(token);
            jweObject.decrypt(new DirectDecrypter(secretKey));
            String jsonPayload = jweObject.getPayload().toString();

            return objectMapper.readValue(jsonPayload, new TypeReference<>() {});
        } catch (Exception e) {
            throw new InvalidTokenException("Invalid token");
        }
    }
}