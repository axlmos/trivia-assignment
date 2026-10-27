package trivia.data;

import trivia.exception.InvalidParameterException;

public enum Difficulty {
    Any("any"),
    Easy("easy"),
    Medium("medium"),
    Hard("hard");

    public final String id;

    private Difficulty(String id) {
        this.id = id;
    }

    public static Difficulty fromId(String id) {
        for (Difficulty difficulty : Difficulty.values()) {
            if (difficulty.id.equals(id)) {
                return difficulty;
            }
        }
        throw new InvalidParameterException("No difficulty with id " + id);
    }
}
