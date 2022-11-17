package ru.yandex.practicum.filmorate.dictionary;

public enum UriParam {
    FILM_ID("идентификатор фильма"),
    USER_ID("идентификатор пользователя"),

    FILM_COUNT("количество фильмов");

    private final String label;

    UriParam(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
