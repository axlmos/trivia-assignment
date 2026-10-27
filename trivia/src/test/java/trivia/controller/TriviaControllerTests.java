package trivia.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;

import trivia.data.*;
import trivia.service.TriviaService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(TriviaController.class)
@AutoConfigureRestTestClient
class TriviaControllerTests {

    @Autowired
    private RestTestClient restTestClient;

    @MockitoBean
    private TriviaService triviaService;

    @Test
    void getQuestions_WithDefaultParameters_ReturnsOk() {
        TriviaQuestionResponse mockResponse = new TriviaQuestionResponse(0, "secret-token", List.of());
        when(triviaService.getQuestions(eq(10), any(Category.class), any(Difficulty.class))).thenReturn(mockResponse);



        restTestClient.get().uri("/questions")
                .exchange()
                .expectStatus().isOk();



        verify(triviaService).getQuestions(eq(10), any(Category.class), any(Difficulty.class));
    }

    @Test
    void getQuestions_WithParameters_ReturnsOk() {
        TriviaQuestionResponse mockResponse = new TriviaQuestionResponse(0, "secret-token", List.of());
        when(triviaService.getQuestions(eq(5), any(), any())).thenReturn(mockResponse);



        restTestClient.get().uri("/questions?amount=5&category=9&difficulty=hard")
                .exchange()
                .expectStatus().isOk();



        verify(triviaService).getQuestions(eq(5), any(Category.class), any(Difficulty.class));
    }

    @Test
    void getQuestions_AmountTooLow_ThrowsIllegalArgumentException() {
        restTestClient.get().uri("/questions?amount=0")
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void getQuestions_AmountTooHigh_ThrowsIllegalArgumentException() {
        restTestClient.get().uri("/questions?amount=51")
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void postCheckAnswers_ValidRequest_ReturnsOk() {
        CheckAnswersBody requestBody = new CheckAnswersBody("token", List.of("Answer"));
        CheckAnswerResponse mockResponse = new CheckAnswerResponse(1, 1);

        when(triviaService.checkAnswers(any(CheckAnswersBody.class))).thenReturn(mockResponse);



        restTestClient.post().uri("/checkanswers")
                .body(requestBody)
                .exchange()
                .expectStatus().isOk();



        verify(triviaService).checkAnswers(any(CheckAnswersBody.class));
    }
}