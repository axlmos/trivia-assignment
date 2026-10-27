package trivia.data;

import org.junit.jupiter.api.Test;
import trivia.exception.InvalidParameterException;

import static org.junit.jupiter.api.Assertions.*;

class DifficultyTest {

    @Test
    void testFromId_ValidIds() {
        assertEquals(Difficulty.Any, Difficulty.fromId("any"));
        assertEquals(Difficulty.Easy, Difficulty.fromId("easy"));
        assertEquals(Difficulty.Medium, Difficulty.fromId("medium"));
        assertEquals(Difficulty.Hard, Difficulty.fromId("hard"));
    }

    @Test
    void testFromId_InvalidIds() {
        assertThrows(
                InvalidParameterException.class,
                () -> Difficulty.fromId("Random")
        );

        assertThrows(
                InvalidParameterException.class,
                () -> Difficulty.fromId("Random2")
        );
    }

    @Test
    void testDifficulty_IdValues() {
        assertEquals("any", Difficulty.Any.id);
        assertEquals("easy", Difficulty.Easy.id);
        assertEquals("medium", Difficulty.Medium.id);
        assertEquals("hard", Difficulty.Hard.id);
    }
}