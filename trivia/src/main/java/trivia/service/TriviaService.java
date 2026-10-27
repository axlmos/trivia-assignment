package trivia.service;

import trivia.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import trivia.exception.*;

import java.util.*;

@Service
public class TriviaService {

    private final ApiService apiService;
    private final JweTokenService jweTokenService;

    /**
     * Constructor of TriviaService
     *
     * @param apiService the ApiService
     * @param jweTokenService the JweTokenService
     */
    @Autowired
    public TriviaService(ApiService apiService, JweTokenService jweTokenService) {
        this.apiService = apiService;
        this.jweTokenService = jweTokenService;
    }

    /**
     * Business logic of getting trivia questions. Calls the OpenTDB API for questions, and then parses it such that
     * only the questions are given, and an encrypted token of the answers.
     *
     * @param amount the amount of questions
     * @param category the category
     * @param difficulty the difficulty
     *
     * @return A response object of the questions
     */
    public TriviaQuestionResponse getQuestions(int amount, Category category, Difficulty difficulty) {

        OpenTDBResponse response = apiService.get(amount, category, difficulty);

        List<TriviaQuestion> questions = new ArrayList<>();
        List<String> correctAnswers = new ArrayList<>();

        for (OpenTDBResult openTDBResult : response.results()) {
            questions.add(mapToTriviaQuestion(openTDBResult));
            correctAnswers.add(openTDBResult.correctAnswer());
        }

        String token = jweTokenService.encryptAnswers(correctAnswers);

        return new TriviaQuestionResponse(0, token, questions);
    }

    /**
     * Business logic for checking answers, the user provides the token and their given answers, and then returns the
     * amount of correct answers
     *
     * @param body The request body
     *
     * @return the score
     */
    public CheckAnswerResponse checkAnswers(CheckAnswersBody body) {
        List<String> userAnswers = body.answers();
        List<String> correctAnswers = jweTokenService.decryptAnswers(body.token());

        if (userAnswers.size() != correctAnswers.size()) {
            throw new InvalidAnswerCountException(String.format("Incorrect amount of answers given, expected: %s but got %s", correctAnswers.size(), userAnswers.size()));
        }

        int score = 0;

        for (int i = 0; i < userAnswers.size(); i++) {
            if (userAnswers.get(i).equals(correctAnswers.get(i))) {
                score++;
            }
        }

        return new CheckAnswerResponse(0, score);
    }

    /**
     * Maps a OpenTDB question to a TriviaQuestion
     *
     * @param openTDBQuestion the question from the openTDB API
     * @return A TriviaQuestion object
     */
    private TriviaQuestion mapToTriviaQuestion(OpenTDBResult openTDBQuestion) {
        List<String> answers = new ArrayList<>(openTDBQuestion.incorrectAnswers());
        answers.add(openTDBQuestion.correctAnswer());
        Collections.shuffle(answers);

        return new TriviaQuestion(openTDBQuestion.type(), openTDBQuestion.question(), answers);
    }

}
