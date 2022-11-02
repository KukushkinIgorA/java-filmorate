package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private int nextId = 1;
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping()
    public List<Film> findAll() {
        log.info("Запрос всех фильмов. Текущее количество {}", films.size());
        return new ArrayList<>(films.values());
    }

    @PostMapping()
    public Film create(@RequestBody Film film) {
        log.info("Запрос на создание фильма {}", film.getName());
        validate(film);
        return createFilm(film);
    }

    @PutMapping()
    public Film update(@RequestBody Film film) {
        log.info("Запрос на обновление фильма {}", film.getId());
        validate(film);
        return updateFilm(film);
    }

    void validate(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.error("название не может быть пустым {}", film);
            throw new ValidationException(HttpStatus.BAD_REQUEST, "название не может быть пустым");
        } else if (film.getDescription() != null && film.getDescription().length() > 200) {
            log.error("максимальная длина описания — 200 символов {}", film);
            throw new ValidationException(HttpStatus.BAD_REQUEST, "максимальная длина описания — 200 символов");
        } else if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))) {
            log.error("дата релиза — не раньше 28 декабря 1895 года {}", film);
            throw new ValidationException(HttpStatus.BAD_REQUEST, "дата релиза — не раньше 28 декабря 1895 года");
        } else if (film.getDuration() <= 0) {
            log.error("продолжительность фильма должна быть положительной {}", film);
            throw new ValidationException(HttpStatus.BAD_REQUEST, "продолжительность фильма должна быть положительной");
        }
    }

    Film createFilm(Film film) {
        int id = nextId++;
        film.setId(id);
        films.put(id, film);
        return films.get(id);
    }

    Film updateFilm(Film film) {
        int id = film.getId();
        if(films.containsKey(id)) {
            films.put(id, film);
        } else {
            log.error("на сервере отстутствует фильм для обновления c id = {}", id);
            throw new ValidationException(HttpStatus.NOT_FOUND, "на сервере отстутствует фильм для обновления");
        }
        return films.get(id);
    }
}
