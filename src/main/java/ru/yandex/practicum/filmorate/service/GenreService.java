package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dictionary.UriParam;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.utils.FilmorateUtils;

import java.util.List;

@Service
@Slf4j
public class GenreService {

    private final GenreStorage genreStorage;

    @Autowired
    public GenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public List<Genre> findAll() {
        return genreStorage.findAll();
    }

    public Genre findGenre(String genreId) {
        int genreIdInt = FilmorateUtils.validateParseInt(genreId, UriParam.GENRE_ID);
        final Genre genre = genreStorage.findGenre(genreIdInt);
        if(genre == null)
            throw new NotFoundException(String.format("на сервере отстутствует жанр c id = %s", genreIdInt));
        return genre;
    }
}
