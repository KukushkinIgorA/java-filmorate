package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dictionary.UriParam;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.storage.RatingMpaStorage;
import ru.yandex.practicum.filmorate.utils.FilmorateUtils;

import java.util.List;

@Service
@Slf4j
public class RatingMpaService {
    private final RatingMpaStorage ratingMpaStorage;

    @Autowired
    public RatingMpaService(RatingMpaStorage ratingMpaStorage) {
        this.ratingMpaStorage = ratingMpaStorage;
    }

    public List<RatingMpa> findAll() {
        return ratingMpaStorage.findAll();
    }

    public RatingMpa findRatingMpa(String ratingMpaId) {
        int ratingMpaIdInt = FilmorateUtils.validateParseInt(ratingMpaId, UriParam.RATING_MPA_ID);
        final RatingMpa ratingMpa = ratingMpaStorage.findRatingMpa(ratingMpaIdInt);
        if(ratingMpa == null)
            throw new NotFoundException(String.format("на сервере отстутствует рейтинг MPA c id = %s", ratingMpaIdInt));
        return ratingMpa;
    }
}
