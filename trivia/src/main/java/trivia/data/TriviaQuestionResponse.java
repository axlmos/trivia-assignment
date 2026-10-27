package trivia.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record TriviaQuestionResponse(
        @JsonProperty("response_code") int responseCode,
        @JsonProperty("token")         String token,
        @JsonProperty("results")       List<TriviaQuestion> results
) {}
