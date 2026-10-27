package trivia.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record TriviaQuestion(
        @JsonProperty("type")     String type,
        @JsonProperty("question") String question,
        @JsonProperty("answers")  List<String> answers
) {}
