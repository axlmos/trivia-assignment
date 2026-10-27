package trivia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import trivia.data.*;
import trivia.exception.InvalidParameterException;
import trivia.service.TriviaService;

@RestController
public class TriviaController {

    private final TriviaService triviaService;

    /**
     * Constructor of the TriviaController
     *
     * @param triviaService the TriviaService
     */
    @Autowired
    public TriviaController(TriviaService triviaService) {
        this.triviaService = triviaService;
    }

    /**
     * Gets a list of questions, including parameters if a certain type of quiz is wanted
     *
     * @param amount (Optional) the amount of questions
     * @param category (Optional) the category of the questions
     * @param difficulty (Optional) the difficulty of the questions
     *
     * @return Response object of questions
     */
    @GetMapping("/questions")
    public ResponseEntity<TriviaQuestionResponse> getQuestions(
            @RequestParam(name = "amount", defaultValue = "10") int amount,
            @RequestParam(name = "category", defaultValue = "0") int category,
            @RequestParam(name = "difficulty", defaultValue = "any") String difficulty
    ) {
        if (amount < 1 || amount > 50) {
            throw new InvalidParameterException("Amount of questions must be in between 1-50 inclusive");
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(triviaService.getQuestions(amount, Category.fromId(category), Difficulty.fromId(difficulty)));
    }

    /**
     * Checks the answers
     *
     * @param body the request body, including the token and given answers
     * @return response object of checking answers
     */
    @PostMapping("/checkanswers")
    public ResponseEntity<CheckAnswerResponse> postCheckAnswers(
            @RequestBody CheckAnswersBody body
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(triviaService.checkAnswers(body));
    }
}