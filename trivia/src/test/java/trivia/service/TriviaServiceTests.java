package trivia.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import trivia.data.*;
import trivia.exception.InvalidAnswerCountException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TriviaServiceTests {

    @Mock
    private ApiService apiService;

    @Mock
    private JweTokenService jweTokenService;

    @InjectMocks
    private TriviaService triviaService;

    @Test
    void getQuestions_shouldMapQuestionsAndReturnToken() {
        int amount = 2;
        Category category = Category.Art;
        Difficulty difficulty = Difficulty.Easy;

        OpenTDBResult result1 = new OpenTDBResult("multiple", "easy", "25", "Who painted the mona lisa?", "DaVinci", List.of("Rembrandt", "Beethoven", "Idk"));
        OpenTDBResult result2 = new OpenTDBResult("multiple", "easy", "25", "What color do you get when you mix blue and red?", "purple", List.of("green", "orange", "red"));
        OpenTDBResponse mockApiResponse = new OpenTDBResponse(0, List.of(result1, result2));

        when(apiService.get(amount, category, difficulty)).thenReturn(mockApiResponse);
        when(jweTokenService.encryptAnswers(List.of("DaVinci", "purple"))).thenReturn("mock-encrypted-token");



        TriviaQuestionResponse response = triviaService.getQuestions(amount, category, difficulty);



        assertEquals("mock-encrypted-token", response.token(), "The response should include the exact token");
        assertEquals(2, response.results().size(), "The response should contain 2 items");

        TriviaQuestion q1 = response.results().get(0);
        assertEquals("Who painted the mona lisa?", q1.question());
        assertEquals(4, q1.answers().size());
        assertTrue(q1.answers().containsAll(List.of("DaVinci", "Rembrandt", "Beethoven", "Idk")));

        assertEquals(0, response.responseCode());

        verify(apiService).get(amount, category, difficulty);
        verify(jweTokenService).encryptAnswers(any());
    }

    @Test
    void checkAnswers_shouldReturnPerfectScoreWhenAllAnswersCorrect() {
        String token = "mock-token";
        List<String> userAnswers = List.of("DaVinci", "purple");
        List<String> correctAnswers = List.of("DaVinci", "purple");

        CheckAnswersBody requestBody = new CheckAnswersBody(token, userAnswers);

        when(jweTokenService.decryptAnswers(token)).thenReturn(correctAnswers);



        CheckAnswerResponse response = triviaService.checkAnswers(requestBody);



        assertEquals(2, response.score());
        assertEquals(0, response.responseCode());
    }

    @Test
    void checkAnswers_shouldReturnPartialScore_whenAnAnswerIncorrect() {
        String token = "mock-token";
        List<String> userAnswers = List.of("Rembrandt", "purple");
        List<String> correctAnswers = List.of("DaVinci", "purple");

        CheckAnswersBody requestBody = new CheckAnswersBody(token, userAnswers);

        when(jweTokenService.decryptAnswers(token)).thenReturn(correctAnswers);



        CheckAnswerResponse response = triviaService.checkAnswers(requestBody);



        assertEquals(1, response.score());
        assertEquals(0, response.responseCode());
    }

    @Test
    void checkAnswers_shouldThrowException_whenAnswerSizeMismatches() {
        String token = "mock-token";
        List<String> userAnswers = List.of("Rembrandt");
        List<String> correctAnswers = List.of("DaVinci", "purple");

        CheckAnswersBody requestBody = new CheckAnswersBody(token, userAnswers);

        when(jweTokenService.decryptAnswers(token)).thenReturn(correctAnswers);



        InvalidAnswerCountException exception = assertThrows(
                InvalidAnswerCountException.class,
                () -> triviaService.checkAnswers(requestBody)
        );

        assertTrue(exception.getMessage().contains("Incorrect amount of answers given"));
        assertTrue(exception.getMessage().contains("expected: 2 but got 1"));
    }
}