package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class FilmControllerMockTest {

    public static final int SIMPLE_INT_DURATION = 37;
    public static final int SIMPLE_INT_RATING = 41;

    @Mock
    private FilmService filmService;

    FilmController filmController;

    @BeforeEach
    void init(){
        filmController = new FilmController(filmService);
    }

    @Test
    void findAll() {
        when(filmService.findAll()).thenReturn(makeFilms());

        assertEquals(makeFilms(), filmService.findAll());
    }

    @Test
    void create() {
        when(filmService.create(any())).thenReturn(makeFilm(1));

        assertEquals(makeFilm(1), filmService.create(makeFilm(1)));
    }

    @Test
    void update() {
        when(filmService.update(any())).thenReturn(makeFilm(1));

        assertEquals(makeFilm(1), filmService.update(makeFilm(1)));
    }

    @Test
    void findFilm() {
        when(filmService.findFilm(any())).thenReturn(makeFilm(1));

        assertEquals(makeFilm(1), filmService.findFilm("1"));
    }

    private List<Film> makeFilms() {
        return Arrays.asList(makeFilm(1), makeFilm(2));
    }

    private Film makeFilm(int filmId){
        return Film.builder()
                .id(filmId)
                .name("name"+filmId)
                .description("description"+filmId)
                .releaseDate(LocalDate.of(2022, 12, 17))
                .duration(SIMPLE_INT_DURATION * filmId)
                .rating(SIMPLE_INT_RATING * filmId)
                .mpa(RatingMpa.builder().id(1).name("G").build())
                .genres(Arrays.asList(Genre.builder().id(1).name("Комедия").build(), Genre.builder().id(2).name("Драма").build()))
                .build();
    }
}