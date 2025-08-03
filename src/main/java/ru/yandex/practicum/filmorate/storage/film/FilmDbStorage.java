package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.base.BaseStorage;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class FilmDbStorage extends BaseStorage<Film> implements FilmStorage {

    private static final String FIND_ALL_QUERY = "select\n" +
            "f.*,\n" +
            "m.mpa_rating_name\n" +
            "from films f \n" +
            "left join mpa_ratings m on f.mpa_rating_id = m.mpa_rating_id";
    private static final String FIND_BY_ID_QUERY = "select\n" +
            "f.*,\n" +
            "m.mpa_rating_name\n" +
            "from films f \n" +
            "left join mpa_ratings m on f.mpa_rating_id = m.mpa_rating_id\n" +
            "where\n" +
            "f.film_id = ?";
    private static final String INSERT_QUERY = "insert into films(name, description, releaseDate, duration, mpa_rating_id)\n" +
            " values (?, ?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "update films set name = ?, description = ?, releaseDate = ?,\n" +
            " duration = ?, mpa_rating_id = ? WHERE film_id = ?";
    private static final String TOP_POPULAR_QUERY = "select\n" +
            "f.*,\n" +
            "m.mpa_rating_name\n" +
            "from films f\n" +
            "left join (select film_id, count(distinct user_id) as likes_count from likes group by film_id) l on f.film_id = l.film_id\n" +
            "left join mpa_ratings m on f.mpa_rating_id = m.mpa_rating_id\n" +
            "order by likes_count desc\n" +
            "limit ?";
    private static final String DELETE_GENRE_FILM_QUERY = "delete from films_genres where film_id = ?";
    private static final String INSERT_GENRE_FILM_QUERY = "insert into films_genres(film_id, genre_id) values(?, ?)";

    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Film createFilm(Film newFilm) {
        long id = insert(
                INSERT_QUERY,
                newFilm.getName(),
                newFilm.getDescription(),
                newFilm.getReleaseDate(),
                newFilm.getDuration(),
                newFilm.getMpa().getId()
        );
        newFilm.setId(id);
        updateGenres(newFilm.getGenres(), newFilm.getId());
        return newFilm;
    }

    @Override
    public Film updateFilm(Film newFilm) {
        update(
                UPDATE_QUERY,
                newFilm.getName(),
                newFilm.getDescription(),
                newFilm.getReleaseDate(),
                newFilm.getDuration(),
                newFilm.getMpa().getId(),
                newFilm.getId()
        );
        updateGenres(newFilm.getGenres(), newFilm.getId());
        return newFilm;
    }

    @Override
    public Optional<Film> getFilmById(long filmId) {
        return findOne(FIND_BY_ID_QUERY, filmId);
    }

    @Override
    public List<Film> getAllFilms() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public List<Film> getTopPopularFilms(int count) {
        return findMany(TOP_POPULAR_QUERY, count);
    }

    @Override
    public boolean existsFilmById(long filmId) {
        return getFilmById(filmId).isPresent();
    }

    public void updateGenres(Set<Genre> genres, Long filmId) {
        delete(DELETE_GENRE_FILM_QUERY, filmId);
        if (!genres.isEmpty()) {
            List<Genre> genresList = genres.stream().toList();
            jdbc.batchUpdate(INSERT_GENRE_FILM_QUERY, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setLong(1, filmId);
                    ps.setInt(2, genresList.get(i).getId());
                }

                @Override
                public int getBatchSize() {
                    return genresList.size();
                }
            });
        }
    }




}
