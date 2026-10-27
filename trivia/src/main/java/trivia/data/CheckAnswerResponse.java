package trivia.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CheckAnswerResponse(
        @JsonProperty("response_code") int responseCode,
        @JsonProperty("score")         int score
) {}
