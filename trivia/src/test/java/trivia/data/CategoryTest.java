package trivia.data;

import org.junit.jupiter.api.Test;
import trivia.exception.InvalidParameterException;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    void testFromId_ValidIds() {
        assertEquals(Category.Any, Category.fromId(0));
        assertEquals(Category.GeneralKnowledge, Category.fromId(9));
        assertEquals(Category.ScienceNature, Category.fromId(17));
        assertEquals(Category.EntertainmentCartoonAnimations, Category.fromId(32));
    }

    @Test
    void testFromId_InvalidIds() {
        assertThrows(
                InvalidParameterException.class,
                () -> Category.fromId(-1)
        );

        assertThrows(
                InvalidParameterException.class,
                () -> Category.fromId(999)
        );
    }

    @Test
    void testCategory_IdValues() {
        assertEquals(0, Category.Any.id);
        assertEquals(19, Category.ScienceMathematics.id);
        assertEquals(24, Category.Politics.id);
    }
}