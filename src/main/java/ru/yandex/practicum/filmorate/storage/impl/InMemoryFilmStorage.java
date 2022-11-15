package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private int nextId = 1;

    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film create(Film film) {
        return  createFilm(film);
    }

    @Override
    public Film update(Film film) {
        return updateFilm(film);
    }

    @Override
    public Film findFilm(int filmId) {
        checkFilmsExist(List.of(filmId));
        return films.get(filmId);
    }

    @Override
    public void userLikesFilm(int filmId, int userId) {
        checkFilmsExist(List.of(filmId));
        films.get(filmId).getLikes().add(userId);
    }

    @Override
    public void userDeleteLike(int filmId, int userId) {
        checkFilmsExist(List.of(filmId));
        films.get(filmId).getLikes().remove(userId);
    }

    @Override
    public List<Film> findMostPopularFilms(int filmCount) {
        return films.values().stream()
                .sorted((p0, p1) -> p1.getLikes().size() - p0.getLikes().size())
                .limit(filmCount)
                .collect(Collectors.toList());
    }

    private Film createFilm(Film film) {
        int id = nextId++;
        film.setId(id);
        films.put(id, film);
        return films.get(id);
    }

    private Film updateFilm(Film film) {
        int id = film.getId();
        checkFilmsExist(List.of(id));
        films.put(id, film);
        return films.get(id);
    }

    private void checkFilmsExist(List<Integer> filmIds) {
        for (Integer filmId : filmIds) {
            if (!films.containsKey(filmId))
                throw new NotFoundException(String.format("на сервере отстутствует фильм c id = %s", filmId));
        }
    }
}
