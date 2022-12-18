package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.service.RatingMpaService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@Slf4j
public class RatingMpaController {
    private final RatingMpaService ratingMpaService;

    @Autowired
    public RatingMpaController(RatingMpaService ratingMpaService) {
        this.ratingMpaService = ratingMpaService;
    }

    //Запрос всех рейтингов MPA
    @GetMapping()
    public List<RatingMpa> findAll() {
        log.info("Запрос всех фильмов.");
        return ratingMpaService.findAll();
    }

    //Найти данные о рейтингe MPA по идентификатору
    @GetMapping("{id}")
    public RatingMpa findRatingMpa (@PathVariable("id") String ratingMpaId) {
        log.info("Запрос фильма по id: {}", ratingMpaId);
        return ratingMpaService.findRatingMpa(ratingMpaId);
    }
}
