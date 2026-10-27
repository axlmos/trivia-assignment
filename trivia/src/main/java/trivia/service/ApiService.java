package trivia.service;

import trivia.data.Category;
import trivia.data.Difficulty;
import trivia.data.OpenTDBResponse;
import org.springframework.web.client.RestClient;
import org.springframework.stereotype.Service;
import trivia.exception.RateLimitException;

@Service
public class ApiService {

    private final RestClient restClient;
    private final String baseUrl = "https://opentdb.com/api.php";

    /**
     * Constructor of ApiService
     */
    public ApiService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

    /**
     * Gets requests from the OpenTDB API with certain parameters
     *
     * @param amount the amount of questions
     * @param category the category of the questions
     * @param difficulty the difficulty of the questions
     * @return A response object of the questions
     */
    public OpenTDBResponse get(int amount, Category category, Difficulty difficulty) {

        String url = baseUrl + "?amount=" + amount;

        if (category != Category.Any) {
            url += "&category=" + category.id;
        }

        if (difficulty != Difficulty.Any) {
            url += "&difficulty=" + difficulty.id;
        }

        return restClient.get()
                .uri(url)
                .retrieve()
                .onStatus(status -> status.value() == 429, (request, response) -> {
                    throw new RateLimitException("Rate limit exceeded.");
                })
                .body(OpenTDBResponse.class);
    }
}