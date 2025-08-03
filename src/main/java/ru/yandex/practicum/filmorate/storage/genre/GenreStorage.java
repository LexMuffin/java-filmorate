package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GenreStorage {

    Optional<Genre> getGenreById(int genreId);

    List<Genre> getAllGenres();

    Set<Genre> getGenresByFilmId(Long filmId);
}
