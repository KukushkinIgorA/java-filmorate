package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FilmControllerTest {
    private final FilmController filmController;
    private final UserStorage userStorage;

    @Test
    public void makeAddFilmWithValidFields() {
        Film film = Film.builder()
                .name("test film name")
                .description("description")
                .duration(100)
                .releaseDate(LocalDate.of(1967, 3, 25))
                .mpa(RatingMpa.builder().id(1).build())
                .genres(Collections.singletonList(Genre.builder().id(1).name("Комедия").build()))
                .build();
        filmController.create(film);

        List<Film> arr = new ArrayList<>(filmController.findAll());

        assertEquals(arr.size(), 1);

        Film filmFromController = arr.get(0);

        assertEquals(filmFromController.getId(), 1);
        assertEquals(filmFromController.getName(), film.getName());
        assertEquals(filmFromController.getDescription(), film.getDescription());
        assertEquals(filmFromController.getDuration(), film.getDuration());
        assertEquals(filmFromController.getReleaseDate(), film.getReleaseDate());
        assertEquals(filmFromController.getMpa().getId(), film.getMpa().getId());
        assertEquals(filmFromController.getGenres().size(), film.getGenres().size());
        assertEquals(filmFromController.getGenres().get(0).getId(), film.getGenres().get(0).getId());
    }

    @Test
    public void makeThrowExceptionIfFilmWithNotValidReleaseDate() {
        Film film = Film.builder()
                .name("test film name")
                .description("description")
                .duration(100)
                .releaseDate(LocalDate.of(1600, 6, 1))
                .mpa(RatingMpa.builder().id(1).build())
                .genres(Collections.singletonList(Genre.builder().id(1).name("Комедия").build()))
                .build();

        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.create(film));
        assertEquals("дата релиза — не раньше 28 декабря 1895 года", exception.getMessage());
        assertEquals(filmController.findAll().size(), 0);
    }

    @Test
    public void makeNoDuplicateGenres()  {
        Film film = Film.builder()
                .name("test film name")
                .description("description")
                .duration(100)
                .releaseDate(LocalDate.of(1967, 3, 25))
                .mpa(RatingMpa.builder().id(1).build())
                .genres(Arrays.asList(Genre.builder().id(1).name("Комедия").build(),
                        Genre.builder().id(2).name("Драма").build(),
                        Genre.builder().id(3).name("Мультфильм").build()))
                .build();

        filmController.create(film);

        Film filmFromController = filmController.findFilm("1");

        assertEquals(filmFromController.getName(), film.getName());
        assertEquals(filmFromController.getDescription(), film.getDescription());
        assertEquals(filmFromController.getDuration(), film.getDuration());
        assertEquals(filmFromController.getReleaseDate(), film.getReleaseDate());
        assertEquals(filmFromController.getMpa().getId(), film.getMpa().getId());
        assertEquals(filmFromController.getGenres().size(), 3);
        assertEquals(filmFromController.getGenres().get(0).getId(), 1);
        assertEquals(filmFromController.getGenres().get(1).getId(), 2);
    }

    @Test
    public void makeUpdateFilm() {
        Film film = Film.builder()
                .id(1)
                .name("test film name")
                .description("description")
                .duration(100)
                .releaseDate(LocalDate.of(1967, 3, 25))
                .mpa(RatingMpa.builder().id(1).build())
                .genres(Collections.singletonList(Genre.builder().id(1).name("Комедия").build()))
                .build();
        filmController.create(film);

        Film updatedFilm = Film.builder()
                .id(1)
                .name("update film name")
                .description("update description")
                .duration(200)
                .releaseDate(LocalDate.of(2000, 1, 1))
                .mpa(RatingMpa.builder().id(2).build())
                .genres(Arrays.asList(Genre.builder().id(2).name("Драма").build(),
                        Genre.builder().id(3).name("Мультфильм").build()))
                .build();
        filmController.update(updatedFilm);

        List<Film> arr = new ArrayList<>(filmController.findAll());

        assertEquals(arr.size(), 1);

        Film filmFromController = arr.get(0);

        assertEquals(filmFromController.getId(), updatedFilm.getId());
        assertEquals(filmFromController.getName(), updatedFilm.getName());
        assertEquals(filmFromController.getDescription(), updatedFilm.getDescription());
        assertEquals(filmFromController.getDuration(), updatedFilm.getDuration());
        assertEquals(filmFromController.getReleaseDate(), updatedFilm.getReleaseDate());
        assertEquals(filmFromController.getMpa().getId(), updatedFilm.getMpa().getId());
        assertEquals(filmFromController.getGenres().size(), updatedFilm.getGenres().size());
        assertEquals(filmFromController.getGenres().get(0).getId(), updatedFilm.getGenres().get(0).getId());
        assertEquals(filmFromController.getGenres().get(1).getId(), updatedFilm.getGenres().get(1).getId());
    }

    @Test
    public void makeThrowExceptionIfUpdateFilmIdNotFound() {
        Film film = Film.builder()
                .id(1)
                .name("test film name")
                .description("description")
                .duration(100)
                .releaseDate(LocalDate.of(1967, 3, 25))
                .mpa(RatingMpa.builder().id(1).build())
                .genres(Collections.singletonList(Genre.builder().id(1).name("Комедия").build()))
                .build();
        filmController.create(film);

        Film updatedFilm = Film.builder()
                .id(999)
                .name("update film name")
                .description("update description")
                .duration(200)
                .releaseDate(LocalDate.of(2000, 1, 1))
                .mpa(RatingMpa.builder().id(2).build())
                .build();

        NotFoundException exception = assertThrows(NotFoundException.class, () -> filmController.update(updatedFilm));
        assertEquals("на сервере отстутствует фильм c id = 999", exception.getMessage());

        List<Film> arr = new ArrayList<>(filmController.findAll());

        assertEquals(arr.size(), 1);

        Film filmFromController = arr.get(0);

        assertEquals(filmFromController.getId(), film.getId());
        assertEquals(filmFromController.getName(), film.getName());
        assertEquals(filmFromController.getDescription(), film.getDescription());
        assertEquals(filmFromController.getDuration(), film.getDuration());
        assertEquals(filmFromController.getReleaseDate(), film.getReleaseDate());
        assertEquals(filmFromController.getMpa().getId(), film.getMpa().getId());
        assertEquals(filmFromController.getGenres().size(), film.getGenres().size());
        assertEquals(filmFromController.getGenres().get(0).getId(), film.getGenres().get(0).getId());
    }

    @Test
    public void makeGetAllFilms() {
        Film film1 = Film.builder()
                .id(1)
                .name("test film name 1")
                .description("description 1")
                .duration(100)
                .releaseDate(LocalDate.of(1967, 3, 25))
                .mpa(RatingMpa.builder().id(1).build())
                .genres(Collections.singletonList(Genre.builder().id(1).name("Комедия").build()))
                .build();
        filmController.create(film1);

        Film film2 = Film.builder()
                .id(2)
                .name("test film name 2")
                .description("description 2")
                .duration(100)
                .releaseDate(LocalDate.of(1987, 8, 5))
                .mpa(RatingMpa.builder().id(1).build())
                .genres(Arrays.asList(Genre.builder().id(2).name("Драма").build(),
                        Genre.builder().id(3).name("Мультфильм").build()))
                .build();
        filmController.create(film2);
        List<Film> arr = filmController.findAll();

        assertEquals(arr.size(), 2);

        Film filmFromController1 = arr.get(0);
        Film filmFromController2 = arr.get(1);

        assertEquals(filmFromController1.getId(), film1.getId());
        assertEquals(filmFromController1.getName(), film1.getName());
        assertEquals(filmFromController1.getDescription(), film1.getDescription());
        assertEquals(filmFromController1.getDuration(), film1.getDuration());
        assertEquals(filmFromController1.getReleaseDate(), film1.getReleaseDate());
        assertEquals(filmFromController1.getMpa().getId(), film1.getMpa().getId());
        assertEquals(filmFromController1.getGenres().size(), film1.getGenres().size());
        assertEquals(filmFromController1.getGenres().get(0).getId(), film1.getGenres().get(0).getId());

        assertEquals(filmFromController2.getId(), film2.getId());
        assertEquals(filmFromController2.getName(), film2.getName());
        assertEquals(filmFromController2.getDescription(), film2.getDescription());
        assertEquals(filmFromController2.getDuration(), film2.getDuration());
        assertEquals(filmFromController2.getReleaseDate(), film2.getReleaseDate());
        assertEquals(filmFromController2.getMpa().getId(), film2.getMpa().getId());
        assertEquals(filmFromController2.getGenres().size(), film2.getGenres().size());
        assertEquals(filmFromController2.getGenres().get(0).getId(), film2.getGenres().get(0).getId());
        assertEquals(filmFromController2.getGenres().get(1).getId(), film2.getGenres().get(1).getId());
    }

    @Test
    public void makeGetEmptyIfNoFilms() {
        List<Film> arr = new ArrayList<>(filmController.findAll());

        assertEquals(arr.size(), 0);
    }

    @Test
    public void makeGetFilmById() {
        Film film = Film.builder()
                .id(1)
                .name("test film name")
                .description("description")
                .duration(100)
                .releaseDate(LocalDate.of(1967, 3, 25))
                .mpa(RatingMpa.builder().id(1).build())
                .genres(Collections.singletonList(Genre.builder().id(1).name("Комедия").build()))
                .build();
        filmController.create(film);
        Film filmFromController = filmController.findFilm("1");

        assertEquals(filmFromController.getId(), film.getId());
        assertEquals(filmFromController.getName(), film.getName());
        assertEquals(filmFromController.getDescription(), film.getDescription());
        assertEquals(filmFromController.getDuration(), film.getDuration());
        assertEquals(filmFromController.getReleaseDate(), film.getReleaseDate());
        assertEquals(filmFromController.getMpa().getId(), film.getMpa().getId());
        assertEquals(filmFromController.getGenres().size(), film.getGenres().size());
        assertEquals(filmFromController.getGenres().get(0).getId(), film.getGenres().get(0).getId());
    }

    @Test
    public void makeThrowExceptionIfFilmIdNotFound() {
        NotFoundException exception = assertThrows(NotFoundException.class, () -> filmController.findFilm("999"));
        assertEquals("на сервере отстутствует фильм c id = 999", exception.getMessage());
    }

    @Test
    public void makeAddLike() {
        Film film1 = Film.builder()
                .id(1)
                .name("test film name")
                .description("description")
                .duration(100)
                .releaseDate(LocalDate.of(1967, 3, 25))
                .mpa(RatingMpa.builder().id(1).build())
                .genres(Collections.singletonList(Genre.builder().id(1).name("Комедия").build()))
                .build();
        filmController.create(film1);

        Film film2 = Film.builder()
                .id(2)
                .name("test film name 2")
                .description("description")
                .duration(100)
                .releaseDate(LocalDate.of(1967, 3, 25))
                .mpa(RatingMpa.builder().id(1).build())
                .genres(Collections.singletonList(Genre.builder().id(2).name("Драма").build()))
                .build();
        filmController.create(film2);

        Film film3 = Film.builder()
                .id(3)
                .name("test film name 3")
                .description("description")
                .duration(100)
                .releaseDate(LocalDate.of(1967, 3, 25))
                .mpa(RatingMpa.builder().id(1).build())
                .genres(Collections.singletonList(Genre.builder().id(3).name("Мультфильм").build()))
                .build();
        filmController.create(film3);

        User user = User.builder()
                .id(1)
                .email("tester@yandex.ru")
                .name("Test name")
                .login("ValidTestLogin")
                .birthday(LocalDate.of(1964, 6, 11))
                .build();
        userStorage.create(user);

        filmController.userLikesFilm("2", "1");

        List<Film> arr = filmController.findMostPopularFilms("1");

        assertEquals(arr.size(), 1);

        Film filmFromController1 = arr.get(0);

        assertEquals(filmFromController1.getId(), film2.getId());
        assertEquals(filmFromController1.getName(), film2.getName());
        assertEquals(filmFromController1.getDescription(), film2.getDescription());
        assertEquals(filmFromController1.getDuration(), film2.getDuration());
        assertEquals(filmFromController1.getReleaseDate(), film2.getReleaseDate());
        assertEquals(filmFromController1.getMpa().getId(), film2.getMpa().getId());
        assertEquals(filmFromController1.getGenres().size(), film2.getGenres().size());
        assertEquals(filmFromController1.getGenres().get(0).getId(), film2.getGenres().get(0).getId());
    }

    @Test
    public void makeThrowExceptionIfLikeNotFoundFilm() {
        User user = User.builder()
                .id(1)
                .email("tester@yandex.ru")
                .name("Test name")
                .login("ValidTestLogin")
                .birthday(LocalDate.of(1964, 6, 11))
                .build();
        userStorage.create(user);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> filmController.userLikesFilm("1", "1"));
        assertEquals("на сервере отстутствует фильм c id = 1", exception.getMessage());
    }

    @Test
    public void makeThrowExceptionIfLikeNotFoundUser() {
        Film film = Film.builder()
                .id(1)
                .name("test film name")
                .description("description")
                .duration(100)
                .releaseDate(LocalDate.of(1967, 3, 25))
                .mpa(RatingMpa.builder().id(1).build())
                .genres(Collections.singletonList(Genre.builder().id(1).name("Комедия").build()))
                .build();
        filmController.create(film);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> filmController.userLikesFilm("1", "1"));
        assertEquals("на сервере отстутствует пользователь c id = 1", exception.getMessage());
    }

    @Test
    public void makeRemoveLike() {
        Film film1 = Film.builder()
                .id(1)
                .name("test film name")
                .description("description")
                .duration(100)
                .releaseDate(LocalDate.of(1967, 3, 25))
                .mpa(RatingMpa.builder().id(1).build())
                .genres(Collections.singletonList(Genre.builder().id(1).name("Комедия").build()))
                .build();
        filmController.create(film1);

        Film film2 = Film.builder()
                .id(2)
                .name("test film name 2")
                .description("description")
                .duration(100)
                .releaseDate(LocalDate.of(1967, 3, 25))
                .mpa(RatingMpa.builder().id(1).build())
                .genres(Collections.singletonList(Genre.builder().id(2).name("Драма").build()))
                .build();
        filmController.create(film2);

        Film film3 = Film.builder()
                .id(3)
                .name("test film name 3")
                .description("description")
                .duration(100)
                .releaseDate(LocalDate.of(1967, 3, 25))
                .mpa(RatingMpa.builder().id(1).build())
                .genres(Collections.singletonList(Genre.builder().id(3).name("Мультфильм").build()))
                .build();
        filmController.create(film3);

        User user = User.builder()
                .id(1)
                .email("tester@yandex.ru")
                .name("Test name")
                .login("ValidTestLogin")
                .birthday(LocalDate.of(1964, 6, 11))
                .build();
        userStorage.create(user);

        filmController.userLikesFilm("2", "1");

        List<Film> arr1 = filmController.findMostPopularFilms("1");

        assertEquals(arr1.size(), 1);

        Film filmFromController1 = arr1.get(0);

        assertEquals(filmFromController1.getId(), film2.getId());
        assertEquals(filmFromController1.getName(), film2.getName());
        assertEquals(filmFromController1.getDescription(), film2.getDescription());
        assertEquals(filmFromController1.getDuration(), film2.getDuration());
        assertEquals(filmFromController1.getReleaseDate(), film2.getReleaseDate());
        assertEquals(filmFromController1.getMpa().getId(), film2.getMpa().getId());
        assertEquals(filmFromController1.getGenres().size(), film2.getGenres().size());
        assertEquals(filmFromController1.getGenres().get(0).getId(), film2.getGenres().get(0).getId());

        filmController.userDeleteLike("2", "1");

        List<Film> arr2 = filmController.findMostPopularFilms("1");

        assertEquals(arr2.size(), 1);

        Film filmFromController2 = arr2.get(0);

        assertEquals(filmFromController2.getId(), film1.getId());
        assertEquals(filmFromController2.getName(), film1.getName());
        assertEquals(filmFromController2.getDescription(), film1.getDescription());
        assertEquals(filmFromController2.getDuration(), film1.getDuration());
        assertEquals(filmFromController2.getReleaseDate(), film1.getReleaseDate());
        assertEquals(filmFromController2.getMpa().getId(), film1.getMpa().getId());
        assertEquals(filmFromController2.getGenres().size(), film1.getGenres().size());
        assertEquals(filmFromController2.getGenres().get(0).getId(), film1.getGenres().get(0).getId());
    }

    @Test
    public void makeThrowExceptionIfDelLikeNotFoundFilm() {
        User user = User.builder()
                .id(1)
                .email("tester@yandex.ru")
                .name("Test name")
                .login("ValidTestLogin")
                .birthday(LocalDate.of(1964, 6, 11))
                .build();
        userStorage.create(user);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> filmController.userDeleteLike("1", "1"));
        assertEquals("на сервере отстутствует фильм c id = 1", exception.getMessage());
    }

    @Test
    public void makeThrowExceptionIfDelLikeNotFoundUser() {
        Film film = Film.builder()
                .id(1)
                .name("test film name")
                .description("description")
                .duration(100)
                .releaseDate(LocalDate.of(1967, 3, 25))
                .mpa(RatingMpa.builder().id(1).build())
                .genres(Collections.singletonList(Genre.builder().id(1).name("Драма").build()))
                .build();
        filmController.create(film);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> filmController.userDeleteLike("1", "1"));
        assertEquals("на сервере отстутствует пользователь c id = 1", exception.getMessage());
    }

    @Test
    public void makeGetTwoTopRatedFilms() {
        Film film1 = Film.builder()
                .id(1)
                .name("test film name 1")
                .description("description")
                .duration(100)
                .releaseDate(LocalDate.of(1967, 3, 25))
                .mpa(RatingMpa.builder().id(1).build())
                .genres(Collections.singletonList(Genre.builder().id(1).name("Комедия").build()))
                .build();
        filmController.create(film1);

        Film film2 = Film.builder()
                .id(2)
                .name("test film name 2")
                .description("description")
                .duration(100)
                .releaseDate(LocalDate.of(1967, 3, 25))
                .mpa(RatingMpa.builder().id(1).build())
                .genres(Collections.singletonList(Genre.builder().id(2).name("Драма").build()))
                .build();
        filmController.create(film2);

        Film film3 = Film.builder()
                .id(3)
                .name("test film name 3")
                .description("description")
                .duration(100)
                .releaseDate(LocalDate.of(1967, 3, 25))
                .mpa(RatingMpa.builder().id(1).build())
                .genres(Collections.singletonList(Genre.builder().id(3).name("Мультфильм").build()))
                .build();
        filmController.create(film3);

        User user1 = User.builder()
                .id(1)
                .email("tester1@yandex.ru")
                .name("Test name 1")
                .login("ValidTestLogin1")
                .birthday(LocalDate.of(1964, 6, 11))
                .build();
        userStorage.create(user1);

        User user2 = User.builder()
                .id(2)
                .email("tester2@yandex.ru")
                .name("Test name 2")
                .login("ValidTestLogin2")
                .birthday(LocalDate.of(1964, 6, 11))
                .build();
        userStorage.create(user2);

        filmController.userLikesFilm("2", "1");
        filmController.userLikesFilm("2", "1");
        filmController.userLikesFilm("3", "2");
        List<Film> arr = filmController.findMostPopularFilms("2");

        assertEquals(arr.size(), 2);

        Film filmFromController1 = arr.get(0);
        Film filmFromController2 = arr.get(1);

        assertEquals(filmFromController1.getId(), film2.getId());
        assertEquals(filmFromController1.getName(), film2.getName());
        assertEquals(filmFromController1.getDescription(), film2.getDescription());
        assertEquals(filmFromController1.getDuration(), film2.getDuration());
        assertEquals(filmFromController1.getReleaseDate(), film2.getReleaseDate());
        assertEquals(filmFromController1.getMpa().getId(), film2.getMpa().getId());
        assertEquals(filmFromController1.getGenres().size(), film2.getGenres().size());
        assertEquals(filmFromController1.getGenres().get(0).getId(), film2.getGenres().get(0).getId());

        assertEquals(filmFromController2.getId(), film3.getId());
        assertEquals(filmFromController2.getName(), film3.getName());
        assertEquals(filmFromController2.getDescription(), film3.getDescription());
        assertEquals(filmFromController2.getDuration(), film3.getDuration());
        assertEquals(filmFromController2.getReleaseDate(), film3.getReleaseDate());
        assertEquals(filmFromController2.getMpa().getId(), film3.getMpa().getId());
        assertEquals(filmFromController2.getGenres().size(), film3.getGenres().size());
        assertEquals(filmFromController2.getGenres().get(0).getId(), film3.getGenres().get(0).getId());
    }

    @Test
    public void makeGetMostTopRateFilm() {
        Film film1 = Film.builder()
                .id(1)
                .name("test film name 1")
                .description("description")
                .duration(100)
                .releaseDate(LocalDate.of(1967, 3, 25))
                .mpa(RatingMpa.builder().id(1).build())
                .genres(Collections.singletonList(Genre.builder().id(1).name("Комедия").build()))
                .build();
        filmController.create(film1);

        Film film2 = Film.builder()
                .id(2)
                .name("test film name 2")
                .description("description")
                .duration(100)
                .releaseDate(LocalDate.of(1967, 3, 25))
                .mpa(RatingMpa.builder().id(1).build())
                .genres(Collections.singletonList(Genre.builder().id(2).name("Драма").build()))
                .build();
        filmController.create(film2);

        Film film3 = Film.builder()
                .id(3)
                .name("test film name 3")
                .description("description")
                .duration(100)
                .releaseDate(LocalDate.of(1967, 3, 25))
                .mpa(RatingMpa.builder().id(1).build())
                .genres(Collections.singletonList(Genre.builder().id(3).name("Мультфильм").build()))
                .build();
        filmController.create(film3);

        User user1 = User.builder()
                .id(1)
                .email("tester1@yandex.ru")
                .name("Test name 1")
                .login("ValidTestLogin1")
                .birthday(LocalDate.of(1964, 6, 11))
                .build();
        userStorage.create(user1);

        User user2 = User.builder()
                .id(2)
                .email("tester2@yandex.ru")
                .name("Test name 2")
                .login("ValidTestLogin2")
                .birthday(LocalDate.of(1964, 6, 11))
                .build();
        userStorage.create(user2);

        filmController.userLikesFilm("2", "1");
        filmController.userLikesFilm("2", "1");
        filmController.userLikesFilm("3", "2");
        List<Film> arr = filmController.findMostPopularFilms("1");

        assertEquals(arr.size(), 1);

        Film filmFromController1 = arr.get(0);

        assertEquals(filmFromController1.getId(), film2.getId());
        assertEquals(filmFromController1.getName(), film2.getName());
        assertEquals(filmFromController1.getDescription(), film2.getDescription());
        assertEquals(filmFromController1.getDuration(), film2.getDuration());
        assertEquals(filmFromController1.getReleaseDate(), film2.getReleaseDate());
        assertEquals(filmFromController1.getMpa().getId(), film2.getMpa().getId());
        assertEquals(filmFromController1.getGenres().size(), film2.getGenres().size());
        assertEquals(filmFromController1.getGenres().get(0).getId(), film2.getGenres().get(0).getId());
    }

    @Test
    public void makeReturnNullIfNoTopRatedFilms() {
        assertEquals(filmController.findMostPopularFilms("10").size(), 0);
    }
}