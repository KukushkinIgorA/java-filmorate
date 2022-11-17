package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.impl.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.impl.InMemoryUserStorage;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class FilmServiceTest {
    FilmService filmService;
    Film film;

    @BeforeEach
    void init() {
        filmService = new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage());
        film = new Film(1, "Зеленая миля", "Пол Эджкомб не верил в чудеса. Пока не столкнулся с одним из них", LocalDate.of(1999, Month.DECEMBER, 6), 189, null);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void validateName(String name) {
        assertDoesNotThrow(() -> filmService.validate(film));
        film.setName(name);
        assertThrows(ValidationException.class, () -> filmService.validate(film));
    }

    @Test
    void validateDescription() {
        assertDoesNotThrow(() -> filmService.validate(film));
        film.setDescription(null);
        assertDoesNotThrow(() -> filmService.validate(film));
        film.setDescription("Пол Эджкомб не верил в чудеса. Пока не столкнулся с одним из нихПол Эджкомб не верил в чудеса. Пока не столкнулся с одним из нихПол Эджкомб не верил в чудеса. Пока не столкнулся с одним из нихПол Эджко");
        assertThrows(ValidationException.class, () -> filmService.validate(film));
    }

    @Test
    void validateBirthday() {
        assertDoesNotThrow(() -> filmService.validate(film));
        film.setReleaseDate(null);
        assertDoesNotThrow(() -> filmService.validate(film));
        film.setReleaseDate(LocalDate.of(1895, Month.DECEMBER, 27));
        assertThrows(ValidationException.class, () -> filmService.validate(film));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -100, Integer.MIN_VALUE})
    void validateDuration(int duration) {
        assertDoesNotThrow(() -> filmService.validate(film));
        film.setDuration(duration);
        assertThrows(ValidationException.class, () -> filmService.validate(film));
    }
}