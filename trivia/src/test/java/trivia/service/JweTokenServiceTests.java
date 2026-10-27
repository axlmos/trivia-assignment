package trivia.service;

import com.nimbusds.jose.JOSEException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import trivia.exception.InvalidTokenException;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JweTokenServiceTests {

    private JweTokenService jweTokenService;

    private final String dummyKey = "12345678901234567890123456789012";

    @BeforeEach
    void setUp() {
        jweTokenService = new JweTokenService(dummyKey);
    }

    @Test
    void shouldEncryptAndDecryptAnswersSuccessfully() {
        List<String> originalAnswers = Arrays.asList("Axl", "Quad", "Solutions");



        String encryptedToken = jweTokenService.encryptAnswers(originalAnswers);
        List<String> decryptedAnswers = jweTokenService.decryptAnswers(encryptedToken);



        assertNotNull(encryptedToken);
        assertNotEquals(originalAnswers.toString(), encryptedToken, "Token should be encrypted");
        assertEquals(originalAnswers, decryptedAnswers, "Decrypted answers should match original");
    }

    @Test
    void shouldThrowRuntimeExceptionWhenEncryptionFailsDueToInvalidKey() {
        JweTokenService invalidKeyService = new JweTokenService("invalid-short-key");
        List<String> answers = Arrays.asList("Axl", "Quad", "Solutions");

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> invalidKeyService.encryptAnswers(answers)
        );

        assertEquals("Failed to encrypt answers", exception.getMessage());
        assertNotNull(exception.getCause());
        assertInstanceOf(JOSEException.class, exception.getCause(), "Cause should be a JOSEException");
    }

    @Test
    void shouldThrowExceptionWhenDecryptingInvalidToken() {
        String invalidToken = "NotAValidJWEToken";



        InvalidTokenException exception = assertThrows(
                InvalidTokenException.class,
                () -> jweTokenService.decryptAnswers(invalidToken)
        );

        assertEquals("Invalid token", exception.getMessage());
    }
}