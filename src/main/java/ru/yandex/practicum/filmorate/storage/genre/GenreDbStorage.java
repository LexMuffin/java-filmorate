package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.base.BaseStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class GenreDbStorage extends BaseStorage<Genre> implements GenreStorage {

    private static final String FIND_ALL_QUERY = "select * from genres";
    private static final String FIND_BY_ID_QUERY = "select * from genres where genre_id = ?";
    private static final String FIND_FILM_GENRES_BY_ID_QUERY = "select\n" +
            "fg.genre_id,\n" +
            "g.genre_name\n" +
            "from films f\n" +
            "join films_genres fg on f.film_id = fg.film_id\n" +
            "join genres g on fg.genre_id = g.genre_id \n" +
            "where\n" +
            "f.film_id = ?";

    public GenreDbStorage(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Optional<Genre> getGenreById(int genreId) {
        return findOne(FIND_BY_ID_QUERY, genreId);
    }

    @Override
    public List<Genre> getAllGenres() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Set<Genre> getGenresByFilmId(Long filmId) {
        return new HashSet<>(findMany(FIND_FILM_GENRES_BY_ID_QUERY, filmId));
    }

}
