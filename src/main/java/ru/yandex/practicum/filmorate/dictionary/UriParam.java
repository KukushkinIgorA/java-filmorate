package ru.yandex.practicum.filmorate.dictionary;

public enum UriParam {
    FILM_ID("идентификатор фильма"),
    USER_ID("идентификатор пользователя"),
    FILM_COUNT("количество фильмов"),

    GENRE_ID("идентификатор жанра"),

    RATING_MPA_ID("идентификатор рейтинга MPA");

    private final String label;

    UriParam(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
