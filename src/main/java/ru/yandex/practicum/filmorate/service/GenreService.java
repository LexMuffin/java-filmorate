package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreStorage genreStorage;

    public Genre getGenreById(int genreId) {
        return genreStorage.getGenreById(genreId).orElseThrow(() -> new GenreNotFoundException("Жанр не найден"));
    }

    public List<Genre> getAllGenres() {
        return genreStorage.getAllGenres();
    }

    public Set<Genre> getGenresByFilmId(Long filmId) {
        return genreStorage.getGenresByFilmId(filmId);
    }
}
