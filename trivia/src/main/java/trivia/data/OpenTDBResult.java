package trivia.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record OpenTDBResult(
        @JsonProperty("type")              String type,
        @JsonProperty("difficulty")        String difficulty,
        @JsonProperty("category")          String category,
        @JsonProperty("question")          String question,
        @JsonProperty("correct_answer")    String correctAnswer,
        @JsonProperty("incorrect_answers") List<String> incorrectAnswers
) {}
