package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    List<Film> findAll();
    Film create(Film film);
    Film update(Film film);
    Film findFilm(int filmId);
    void userLikesFilm(int filmId, int userId);
    void userDeleteLike(int filmId, int userId);
    List<Film> findMostPopularFilms(int filmCount);
}