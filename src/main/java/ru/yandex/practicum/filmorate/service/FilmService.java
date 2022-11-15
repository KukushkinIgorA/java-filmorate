package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Service
@Slf4j
public class FilmService {
    public static final int DEFAULT_FILM_COUNT = 10;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }


    public List<Film> findAll() {
        return filmStorage.findAll();
    }


    public Film create(Film film) {
        validate(film);
        return  filmStorage.create(film);
    }

    public Film update(Film film) {
        validate(film);
        return filmStorage.update(film);
    }

    public Film findFilm(String filmId) {
        int filmIdInt = Integer.parseInt(filmId);
        return filmStorage.findFilm(filmIdInt);
    }

    public void userLikesFilm(String filmId, String userId) {
        int filmIdInt = Integer.parseInt(filmId);
        int userIdInt = Integer.parseInt(userId);
        userStorage.findUser(userIdInt);
        filmStorage.userLikesFilm(filmIdInt, userIdInt);
    }
    public void userDeleteLike(String filmId, String userId) {
        int filmIdInt = Integer.parseInt(filmId);
        int userIdInt = Integer.parseInt(userId);
        userStorage.findUser(userIdInt);
        filmStorage.userDeleteLike(filmIdInt, userIdInt);
    }

    public List<Film> findMostPopularFilms(String filmCount) {
        int filmCountInt;
        if (filmCount == null) {
            filmCountInt = DEFAULT_FILM_COUNT;
        } else {
            filmCountInt = Integer.parseInt(filmCount);
        }
        return filmStorage.findMostPopularFilms(filmCountInt);
    }

    void validate(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.error("название не может быть пустым {}", film);
            throw new ValidationException("название не может быть пустым");
        } else if (film.getDescription() != null && film.getDescription().length() > 200) {
            log.error("максимальная длина описания — 200 символов {}", film);
            throw new ValidationException("максимальная длина описания — 200 символов");
        } else if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))) {
            log.error("дата релиза — не раньше 28 декабря 1895 года {}", film);
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года");
        } else if (film.getDuration() <= 0) {
            log.error("продолжительность фильма должна быть положительной {}", film);
            throw new ValidationException("продолжительность фильма должна быть положительной");
        }
    }
}
