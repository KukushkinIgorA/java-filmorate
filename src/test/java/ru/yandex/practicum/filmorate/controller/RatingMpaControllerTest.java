package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.RatingMpa;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class RatingMpaControllerTest {

    private final RatingMpaController ratingMpaController;

    @Test
    void findAll() {
        List<RatingMpa> ratingMpas = ratingMpaController.findAll();

        assertEquals(5, ratingMpas.size());

        RatingMpa ratingMpa1 = ratingMpas.get(0);
        RatingMpa ratingMpa2 = ratingMpas.get(1);
        RatingMpa ratingMpa3 = ratingMpas.get(2);
        RatingMpa ratingMpa4 = ratingMpas.get(3);
        RatingMpa ratingMpa5 = ratingMpas.get(4);

        assertEquals(1, ratingMpa1.getId());
        assertEquals("G", ratingMpa1.getName());
        //assertEquals("У фильма нет возрастных ограничений", ratingMpa1.getDescription());
        assertEquals(2, ratingMpa2.getId());
        assertEquals("PG", ratingMpa2.getName());
        //assertEquals("Детям рекомендуется смотреть фильм с родителями", ratingMpa2.getDescription());
        assertEquals(3, ratingMpa3.getId());
        assertEquals("PG-13", ratingMpa3.getName());
        //assertEquals("Детям до 13 лет просмотр не желателен", ratingMpa3.getDescription());
        assertEquals(4, ratingMpa4.getId());
        assertEquals("R", ratingMpa4.getName());
        //assertEquals("Лицам до 17 лет просматривать фильм можно только в присутствии", ratingMpa4.getDescription());
        assertEquals(5, ratingMpa5.getId());
        assertEquals("NC-17", ratingMpa5.getName());
        //assertEquals("Лицам до 18 лет просмотр запрещён", ratingMpa5.getDescription());
    }

    @Test
    void findRatingMpa() {
        RatingMpa ratingMpa = ratingMpaController.findRatingMpa("5");

        assertEquals(5, ratingMpa.getId());
        assertEquals("NC-17", ratingMpa.getName());
        //assertEquals("Лицам до 18 лет просмотр запрещён", ratingMpa.getDescription());
    }

    @Test
    public void findRatingMpaNotFound() {
        NotFoundException exception = assertThrows(NotFoundException.class, () -> ratingMpaController.findRatingMpa("7"));
        assertEquals("на сервере отстутствует рейтинг MPA c id = 7", exception.getMessage());
    }
}