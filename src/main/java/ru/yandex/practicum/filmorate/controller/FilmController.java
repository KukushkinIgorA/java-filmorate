package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    //Запрос всех фильмов
    @GetMapping()
    public List<Film> findAll() {
        log.info("Запрос всех фильмов.");
        return filmService.findAll();
    }

    //Запрос на создание фильма
    @PostMapping()
    public Film create(@RequestBody Film film) {
        log.info("Запрос на создание фильма {}", film.getName());
        return filmService.create(film);
    }

    //Запрос на обновление фильма
    @PutMapping()
    public Film update(@RequestBody Film film) {
        log.info("Запрос на обновление фильма {}", film.getId());
        return filmService.update(film);
    }

    //Найти данные о пользователе по идентификатору
    @GetMapping("{id}")
    public Film findFilm (@PathVariable("id") String filmId) {
        log.info("Запрос фильма по id: {}", filmId);
        return filmService.findFilm(filmId);
    }

    //Пользователь ставит лайк фильму
    @PutMapping("{id}/like/{userId}")
    public void userLikesFilm(
            @PathVariable("id") String filmId,
            @PathVariable() String userId) {
        log.info("Пользователь {} ставит лайк фильму {}", userId, filmId);
        filmService.userLikesFilm(filmId, userId);
    }

    //пользователь удаляет лайк
    @DeleteMapping("{id}/like/{userId}")
    public void userDeleteLike(
            @PathVariable("id") String filmId,
            @PathVariable() String userId) {
        log.info("Пользователь {} удаляет лайк у фильма {}", userId, filmId);
        filmService.userDeleteLike(filmId, userId);
    }

    //Возвращает список из первых count фильмов по количеству лайков
    @GetMapping("popular")
    public List<Film> findMostPopularFilms(@RequestParam(name = "count", required = false) String filmCount) {
        log.info("Запрос первых {} фильмов по количеству лайков", filmCount);
        return filmService.findMostPopularFilms(filmCount);
    }
}
