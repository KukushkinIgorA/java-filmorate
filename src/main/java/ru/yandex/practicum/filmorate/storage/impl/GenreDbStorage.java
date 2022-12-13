package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository("genreDbStorage")
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> findAll() {
        String sqlQuery = "select GENRE_ID, NAME "+
                "from GENRE order by GENRE_ID";
        return jdbcTemplate.query(sqlQuery, GenreDbStorage::makeGenre);
    }

    @Override
    public Genre findGenre(int genreId) {
        String sqlQuery = "select GENRE_ID, NAME "+
                "from GENRE where GENRE_ID = ?";
        final List<Genre> genres = jdbcTemplate.query(sqlQuery, GenreDbStorage::makeGenre, genreId);
        if(genres.size() == 0){
            return null;
        } else {
            return genres.get(0);
        }
    }

    private static Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(
                rs.getInt("GENRE_ID"),
                rs.getString("NAME")
        );
    }
}
