package trivia.data;

import trivia.exception.InvalidParameterException;

public enum Category {
    Any(0),
    GeneralKnowledge(9),
    EntertainmentBooks(10),
    EntertainmentFilm(11),
    EntertainmentMusic(12),
    EntertainmentMusicalsTheatres(13),
    EntertainmentTelevision(14),
    EntertainmentVideoGames(15),
    EntertainmentBoardGames(16),
    ScienceNature(17),
    ScienceComputers(18),
    ScienceMathematics(19),
    Mythology(20),
    Sports(21),
    Geography(22),
    History(23),
    Politics(24),
    Art(25),
    Celebrities(26),
    Animals(27),
    Vehicles(28),
    EntertainmentComics(29),
    ScienceGadgets(30),
    EntertainmentJapaneseAnimeManga(31),
    EntertainmentCartoonAnimations(32);

    public final int id;

    private Category(int id) {
        this.id = id;
    }

    public static Category fromId(int id) {
        for (Category category : Category.values()) {
            if (category.id == id) {
                return category;
            }
        }
        throw new InvalidParameterException("No category with id " + id);
    }
}