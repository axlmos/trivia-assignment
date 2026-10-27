package trivia.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record CheckAnswersBody(
        @JsonProperty("token")   String token,
        @JsonProperty("answers") List<String> answers
) {}
