package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmLike;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Repository("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Film> findAll() {
        String sqlQuery = "select f.FILM_ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.RATING, mpa.RATING_MPA_ID, mpa.NAME, g.GENRE_ID, g.NAME " +
                "from FILMS f " +
                "left join RATING_MPA mpa on f.RATING_MPA_ID = mpa.RATING_MPA_ID " +
                "left join FILM_GENRE fg on f.FILM_ID = fg.FILM_ID " +
                "left join GENRE g on fg.GENRE_ID = g.GENRE_ID";
        final List<Film> films = jdbcTemplate.query(sqlQuery, FilmDbStorage::makeFilm);
        return distinctFilms(films);
    }

    @Override
    @Transactional
    public Film create(Film film) {
        String sqlInsert = "insert into FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATING_MPA_ID) values (?, ?, ?, ?, ?)";
        String sqlInsertGenre = "insert into FILM_GENRE (FILM_ID, GENRE_ID) values (?, ?) on conflict do nothing";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement stmt = con.prepareStatement(sqlInsert, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            final LocalDate releaseDate = film.getReleaseDate();
            if (releaseDate == null) {
                stmt.setNull(3, Types.DATE);
            } else {
                stmt.setDate(3, Date.valueOf(releaseDate));
            }
            stmt.setInt(4, film.getDuration());
            if (film.getMpa() != null) {
                stmt.setInt(5, film.getMpa().getId());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }

            return stmt;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());

        insertGenres(film, sqlInsertGenre);
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
        String sqlInsertGenre = "insert into FILM_GENRE (FILM_ID, GENRE_ID) values (?, ?) on conflict do nothing";

        int updateRecordCount = jdbcTemplate.update(sqlUpdate, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getMpa() != null ? film.getMpa().getId() : null,
                film.getId());
        if (updateRecordCount != 1) {
            return null;
        } else {
            jdbcTemplate.update(sqlDelete, film.getId());

            insertGenres(film, sqlInsertGenre);
            return film;

        }
    }

    @Override
    public Film findFilm(int filmId) {
        String sqlQuery = "select f.FILM_ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.RATING, mpa.RATING_MPA_ID, mpa.NAME, g.GENRE_ID, g.NAME " +
                "from FILMS f left join RATING_MPA mpa on f.RATING_MPA_ID = mpa.RATING_MPA_ID " +
                "left join FILM_GENRE fg on f.FILM_ID = fg.FILM_ID " +
                "left join GENRE g on fg.GENRE_ID = g.GENRE_ID " +
                "where f.FILM_ID = ?";
        final List<Film> films = jdbcTemplate.query(sqlQuery, FilmDbStorage::makeFilm, filmId);
        if (films.size() == 0) {
            return null;
        } else {
            return distinctFilms(films).get(0);
        }
    }

    @Override
    @Transactional
    public void userLikesFilm(int filmId, int userId) {
        String sqlQuery = "select FILM_LIKE_ID, FILM_ID, USER_ID " +
                "from FILM_LIKE " +
                "where FILM_ID = ? AND USER_ID = ?";
        String sqlInsert = "insert into FILM_LIKE (FILM_ID, USER_ID) values (?, ?)";
        String sqlUpdate = "update FILMS set RATING = RATING+1 where FILM_ID = ?";

        final List<FilmLike> filmLikes = jdbcTemplate.query(sqlQuery, FilmDbStorage::makeFilmLike, filmId, userId);

        if (filmLikes.size() == 0) {
            jdbcTemplate.update(sqlInsert, filmId, userId);
            jdbcTemplate.update(sqlUpdate, filmId);
        }
    }

    @Override
    @Transactional
    public void userDeleteLike(int filmId, int userId) {
        String sqlQuery = "select FILM_LIKE_ID, FILM_ID, USER_ID " +
                "from FILM_LIKE " +
                "where FILM_ID = ? AND USER_ID = ?";
        String sqlDelete = "delete from FILM_LIKE where FILM_ID = ? AND USER_ID = ?";
        String sqlUpdate = "update FILMS set RATING = RATING-1 where FILM_ID = ?";

        final List<FilmLike> filmLikes = jdbcTemplate.query(sqlQuery, FilmDbStorage::makeFilmLike, filmId, userId);

        if (filmLikes.size() != 0) {
            jdbcTemplate.update(sqlDelete, filmId, userId);
            jdbcTemplate.update(sqlUpdate, filmId);
        }
    }

    @Override
    public List<Film> findMostPopularFilms(int filmCount) {
        String sqlQuery = "select f.FILM_ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.RATING, mpa.RATING_MPA_ID, mpa.NAME, g.GENRE_ID, g.NAME " +
                "from FILMS f left join FILM_LIKE fl on f.FILM_ID = fl.FILM_ID " +
                "left join RATING_MPA mpa on f.RATING_MPA_ID = mpa.RATING_MPA_ID " +
                "left join FILM_GENRE fg on f.FILM_ID = fg.FILM_ID " +
                "left join GENRE g on fg.GENRE_ID = g.GENRE_ID";
        final List<Film> films = jdbcTemplate.query(sqlQuery, FilmDbStorage::makeFilm);
        return distinctFilms(films).stream().sorted(Comparator.comparingInt(Film::getRating).reversed()).limit(filmCount).collect(Collectors.toList());
    }

    private static Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        return new Film(
                rs.getInt("FILM_ID"),
                rs.getString("FILMS.NAME"),
                rs.getString("DESCRIPTION"),
                rs.getDate("RELEASE_DATE").toLocalDate(),
                rs.getInt("DURATION"),
                rs.getInt("RATING"),
                makeRatingMpaMap(rs.getInt("RATING_MPA.RATING_MPA_ID"), rs.getString("RATING_MPA.NAME")),
                makeGenres(rs.getInt("GENRE.GENRE_ID"), rs.getString("GENRE.NAME"))
        );
    }

    private static FilmLike makeFilmLike(ResultSet rs, int rowNum) throws SQLException {
        return new FilmLike(
                rs.getInt("FILM_LIKE_ID"),
                rs.getInt("FILM_ID"),
                rs.getInt("USER_ID")
        );
    }

    private static RatingMpa makeRatingMpaMap(int ratingMpaId, String ratingMpaName) {
        RatingMpa mpa = new RatingMpa();
        if (ratingMpaId != 0) {
            mpa.setId(ratingMpaId);
            mpa.setName(ratingMpaName);
        }
        return mpa;
    }

    private static List<Genre> makeGenres(int genreId, String genreName){
        Genre genre = new Genre(genreId, genreName);
        List<Genre> genres = new ArrayList<>();
        if (genreId != 0) {
            genres.add(genre);
        }
        return genres;
    }

    private static List<Film> distinctFilms(List<Film> films) {
        Map<Integer, Film> filmsMap = new HashMap<>();
        for (Film film : films) {
            if (filmsMap.containsKey(film.getId())) {

                filmsMap.get(film.getId()).getGenres().add(film.getGenres().get(0));
            } else {
                filmsMap.put(film.getId(), film);
            }
        }
        return new ArrayList<>(filmsMap.values());
    }

    private void insertGenres(Film film, String sqlInsertGenre) {
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            List<Integer> genreIds = film.getGenres().stream().map(Genre::getId).distinct().collect(Collectors.toList());
            jdbcTemplate.batchUpdate(sqlInsertGenre, genreIds, 100,
                    (PreparedStatement ps, Integer genreId) -> {
                        ps.setInt(1, film.getId());
                        ps.setInt(2, genreId);
                    });
            //Чтобы тест не валился, в базу уже отфильтровали genreIds и на вставку стоит UNIQUE(film_id, genre_id)
            List<Genre> genres = genreIds.stream().map(e->new Genre(e, null)).collect(Collectors.toList());
            film.setGenres(genres);
        }
    }
}
