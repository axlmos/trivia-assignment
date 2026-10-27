package trivia.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import trivia.data.Category;
import trivia.data.Difficulty;
import trivia.data.OpenTDBResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class ApiServiceTests {

    private ApiService apiService;
    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder();
        this.mockServer = MockRestServiceServer.bindTo(builder).build();
        this.apiService = new ApiService(builder);
    }

    @Test
    void get_shouldBuildCorrectUrlWithAnyCategoryAndDifficulty() {
        String expectedUrl = "https://opentdb.com/api.php?amount=10";
        String mockJsonResponse = "{\"response_code\":0,\"results\":[]}";

        mockServer.expect(requestTo(expectedUrl))
                .andRespond(withSuccess(mockJsonResponse, MediaType.APPLICATION_JSON));

        OpenTDBResponse actualResponse = apiService.get(10, Category.Any, Difficulty.Any);

        assertNotNull(actualResponse);
        assertEquals(0, actualResponse.responseCode());
        mockServer.verify();
    }

    @Test
    void get_shouldBuildCorrectUrlWithSpecificCategoryAndDifficulty() {
        Category mockCategory = Category.Art;
        Difficulty mockDifficulty = Difficulty.Hard;

        String expectedUrl = "https://opentdb.com/api.php?amount=5&category=" + mockCategory.id + "&difficulty=" + mockDifficulty.id;
        String mockJsonResponse = "{\"response_code\":0,\"results\":[]}";

        mockServer.expect(requestTo(expectedUrl))
                .andRespond(withSuccess(mockJsonResponse, MediaType.APPLICATION_JSON));

        OpenTDBResponse actualResponse = apiService.get(5, mockCategory, mockDifficulty);

        assertNotNull(actualResponse);
        assertEquals(0, actualResponse.responseCode());
        mockServer.verify();
    }

    @Test
    void get_shouldThrowRateLimitExceptionOn429() {
        String expectedUrl = "https://opentdb.com/api.php?amount=10";

        mockServer.expect(requestTo(expectedUrl))
                .andRespond(org.springframework.test.web.client.response.MockRestResponseCreators.withStatus(org.springframework.http.HttpStatus.TOO_MANY_REQUESTS));

        trivia.exception.RateLimitException exception = org.junit.jupiter.api.Assertions.assertThrows(
                trivia.exception.RateLimitException.class,
                () -> apiService.get(10, Category.Any, Difficulty.Any)
        );

        assertEquals("Rate limit exceeded.", exception.getMessage());
        mockServer.verify();
    }
}