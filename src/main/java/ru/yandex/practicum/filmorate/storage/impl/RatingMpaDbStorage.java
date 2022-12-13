package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.storage.RatingMpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository("ratingMpaDbStorage")
public class RatingMpaDbStorage implements RatingMpaStorage {
    private final JdbcTemplate jdbcTemplate;

    public RatingMpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<RatingMpa> findAll() {
        String sqlQuery = "select RATING_MPA_ID, NAME, DESCRIPTION "+
                "from RATING_MPA";
        return jdbcTemplate.query(sqlQuery, RatingMpaDbStorage::makeRatingMpa);
    }

    @Override
    public RatingMpa findRatingMpa(int ratingMpaId) {
        String sqlQuery = "select RATING_MPA_ID, NAME, DESCRIPTION "+
                "from RATING_MPA where RATING_MPA_ID = ?";
        final List<RatingMpa> ratingMpas = jdbcTemplate.query(sqlQuery, RatingMpaDbStorage::makeRatingMpa, ratingMpaId);
        if(ratingMpas.size() == 0){
            return null;
        } else {
            return ratingMpas.get(0);
        }
    }

    private static RatingMpa makeRatingMpa(ResultSet rs, int rowNum) throws SQLException {
        return new RatingMpa(
                rs.getInt("RATING_MPA_ID"),
                rs.getString("NAME"),
                rs.getString("DESCRIPTION")
        );
    }
}
