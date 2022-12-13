package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class GenreControllerTest {
    private final GenreController genreController;

    @Test
    void findAll() {
        List<Genre> genres = genreController.findAll();

        assertEquals(6, genres.size());

        Genre genre1 = genres.get(0);
        Genre genre2 = genres.get(1);
        Genre genre3 = genres.get(2);
        Genre genre4 = genres.get(3);
        Genre genre5 = genres.get(4);
        Genre genre6 = genres.get(5);

        assertEquals(1, genre1.getId());
        //assertEquals("Комедия", genre1.getName());
        assertEquals(2, genre2.getId());
        //assertEquals("Драма", genre2.getName());
        assertEquals(3, genre3.getId());
        //assertEquals("Мультфильм", genre3.getName());
        assertEquals(4, genre4.getId());
        //assertEquals("Триллер", genre4.getName());
        assertEquals(5, genre5.getId());
        //assertEquals("Документальный", genre5.getName());
        assertEquals(6, genre6.getId());
        //assertEquals("Боевик", genre6.getName());
    }

    @Test
    void findGenre() {
        Genre genre = genreController.findGenre("1");

        assertEquals(1, genre.getId());
        //assertEquals("Комедия", genre.getName());
    }

    @Test
    public void findGenreNotFound() {
        NotFoundException exception = assertThrows(NotFoundException.class, () -> genreController.findGenre("7"));
        assertEquals("на сервере отстутствует жанр c id = 7", exception.getMessage());
    }
}