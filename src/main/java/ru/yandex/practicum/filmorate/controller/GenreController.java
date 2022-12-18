package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@RestController
@RequestMapping("/genres")
@Slf4j
public class GenreController {

    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    //Запрос всех жанров
    @GetMapping()
    public List<Genre> findAll() {
        log.info("Запрос всех фильмов.");
        return genreService.findAll();
    }

    //Найти данные о жанре по идентификатору
    @GetMapping("{id}")
    public Genre findGenre (@PathVariable("id") String genreId) {
        log.info("Запрос фильма по id: {}", genreId);
        return genreService.findGenre(genreId);
    }
}
