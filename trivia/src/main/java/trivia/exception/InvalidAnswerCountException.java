package trivia.exception;

public class InvalidAnswerCountException extends RuntimeException {
    public InvalidAnswerCountException(String message) {
        super(message);
    }
}
