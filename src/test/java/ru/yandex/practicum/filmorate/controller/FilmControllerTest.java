package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    FilmController filmController;
    Film film;

    @BeforeEach
    void init() {
        filmController = new FilmController();
        film = new Film(1, "Зеленая миля", "Пол Эджкомб не верил в чудеса. Пока не столкнулся с одним из них", LocalDate.of(1999, Month.DECEMBER, 6), 189);
    }

    @Test
    void validateName() {
        assertDoesNotThrow(() -> filmController.validate(film));
        film.setName("");
        assertThrows(ValidationException.class, () -> filmController.validate(film));
        film.setName(null);
        assertThrows(ValidationException.class, () -> filmController.validate(film));
    }

    @Test
    void validateDescription() {
        assertDoesNotThrow(() -> filmController.validate(film));
        film.setDescription(null);
        assertDoesNotThrow(() -> filmController.validate(film));
        film.setDescription("Пол Эджкомб не верил в чудеса. Пока не столкнулся с одним из нихПол Эджкомб не верил в чудеса. Пока не столкнулся с одним из нихПол Эджкомб не верил в чудеса. Пока не столкнулся с одним из нихПол Эджко");
        assertThrows(ValidationException.class, () -> filmController.validate(film));
    }

    @Test
    void validateBirthday() {
        assertDoesNotThrow(() -> filmController.validate(film));
        film.setReleaseDate(null);
        assertDoesNotThrow(() -> filmController.validate(film));
        film.setReleaseDate(LocalDate.of(1895, Month.DECEMBER, 27));
        assertThrows(ValidationException.class, () -> filmController.validate(film));
    }

    @Test
    void validateDuration() {
        assertDoesNotThrow(() -> filmController.validate(film));
        film.setDuration(0);
        assertThrows(ValidationException.class, () -> filmController.validate(film));
        film.setDuration(-1);
        assertThrows(ValidationException.class, () -> filmController.validate(film));
    }
}