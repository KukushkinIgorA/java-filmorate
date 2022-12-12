package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmLike;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Film> findAll() {
        String sqlQuery = "select FILM_ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION "+
                "from FILMS";
        final List<Film> films = jdbcTemplate.query(sqlQuery, FilmDbStorage::makeFilm);
        return films;
    }

    @Override
    @Transactional
    public Film create(Film film) {
        String sqlInsert = "insert into FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATING_MPA_ID) values (?, ?, ?, ?, ?)";
        String sqlInsertGenre = "insert into FILM_GENRE (FILM_ID, GENRE_ID) values (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement stmt = con.prepareStatement(sqlInsert, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            final LocalDate releaseDate = film.getReleaseDate();
            if (releaseDate == null){
                stmt.setNull(3, Types.DATE);
            } else {
                stmt.setDate(3, Date.valueOf(releaseDate));
            }
            stmt.setInt(4, film.getDuration());
            if(!film.getMpa().isEmpty() && film.getMpa() != null) {
                stmt.setInt(5, film.getMpa().get("id"));
            }
            return stmt;
        }, keyHolder);

        List<Integer> genreIds = film.getGenres().stream().map(e->e.get("id")).collect(Collectors.toList());
        film.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        jdbcTemplate.batchUpdate(sqlInsertGenre, genreIds, 100,
                (PreparedStatement ps, Integer genreId) -> {
                    ps.setInt(1, film.getId());
                    ps.setInt(2, genreId);
                });
        return film;
    }

    @Override
    @Transactional
    public Film update(Film film) {
        String sqlUpdate = "update FILMS " +
                "set NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, RATING_MPA_ID = ? " +
                "where FILM_ID = ?";
        String sqlDelete = "delete from FILM_GENRE " +
                "where FILM_ID = ?";
        String sqlInsertGenre = "insert into FILM_GENRE (FILM_ID, GENRE_ID) values (?, ?)";

        int updateRecordCount = jdbcTemplate.update(sqlUpdate, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                !film.getMpa().isEmpty() && film.getMpa() != null ? film.getMpa().get("id"): null,
                film.getId());
        if (updateRecordCount != 1) {
            return null;
        } else {
            jdbcTemplate.update(sqlDelete, film.getId());

            List<Integer> genreIds = film.getGenres().stream().map(e->e.get("id")).collect(Collectors.toList());
            jdbcTemplate.batchUpdate(sqlInsertGenre, genreIds, 100,
                    (PreparedStatement ps, Integer genreId) -> {
                        ps.setInt(1, film.getId());
                        ps.setInt(2, genreId);
                    });
            return film;
        }
    }

    @Override
    public Film findFilm(int filmId) {
        String sqlQuery = "select FILM_ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION "+
                "from FILMS "+
                "where FILM_ID = ?";
        final List<Film> films = jdbcTemplate.query(sqlQuery, FilmDbStorage::makeFilm, filmId);
        if(films.size() == 0){
            return null;
        } else {
            return films.get(0);
        }
    }

    @Override
    public void userLikesFilm(int filmId, int userId) {
        String sqlQuery = "select FILM_LIKE_ID, FILM_ID, USER_ID " +
                "from FILM_LIKE " +
                "where FILM_ID = ? AND USER_ID = ?";
        String sqlInsert = "insert into FILM_LIKE (FILM_ID, USER_ID) values (?, ?)";

        final List<FilmLike> filmLikes = jdbcTemplate.query(sqlQuery, FilmDbStorage::makeFilmLike, filmId, userId);

        if(filmLikes.size() == 0){
            jdbcTemplate.update(sqlInsert, filmId, userId);
        }
    }

    @Override
    public void userDeleteLike(int filmId, int userId) {
        String sqlQuery = "select FILM_LIKE_ID, FILM_ID, USER_ID " +
                "from FILM_LIKE " +
                "where FILM_ID = ? AND USER_ID = ?";
        String sqlDelete = "delete from FILM_LIKE where FILM_ID = ? AND USER_ID = ?";

        final List<FilmLike> filmLikes = jdbcTemplate.query(sqlQuery, FilmDbStorage::makeFilmLike, filmId, userId);

        if(filmLikes.size() != 0){
            jdbcTemplate.update(sqlDelete, filmId, userId);
        }
    }

    @Override
    public List<Film> findMostPopularFilms(int filmCount) {
        String sqlQuery = "select f.FILM_ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, count(fl.FILM_ID) rating " +
                "from FILMS f LEFT JOIN FILM_LIKE fl on f.FILM_ID = fl.FILM_ID " +
                "group by f.FILM_ID " +
                "order by rating desc " +
                "limit ?";
        final List<Film> films = jdbcTemplate.query(sqlQuery, FilmDbStorage::makeFilm, filmCount);
        return films;
    }

    static Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        return new Film(
                rs.getInt("FILM_ID"),
                rs.getString("NAME"),
                rs.getString("DESCRIPTION"),
                rs.getDate("RELEASE_DATE").toLocalDate(),
                rs.getInt("DURATION")
        );
    }

    static FilmLike makeFilmLike(ResultSet rs, int rowNum) throws SQLException {
        return new FilmLike(
                rs.getInt("FILM_LIKE_ID"),
                rs.getInt("FILM_ID"),
                rs.getInt("USER_ID")
        );
    }
}
