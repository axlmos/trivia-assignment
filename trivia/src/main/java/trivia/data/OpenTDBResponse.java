package trivia.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record OpenTDBResponse(
        @JsonProperty("response_code") int responseCode,
        @JsonProperty("results")       List<OpenTDBResult> results
) {}
