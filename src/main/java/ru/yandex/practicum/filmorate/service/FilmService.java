package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikeService likeService;
    private final MpaService mpaService;
    private final GenreService genreService;

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(Long filmId) {
        Film film = filmStorage.getFilmById(filmId).orElseThrow(() -> new NotFoundException("Фильм не найдем"));
        film.setGenres(genreService.getGenresByFilmId(film.getId()));
        return film;
    }

    public Film createFilm(Film newFilm) {
        if (newFilm.getMpa() != null) {
            mpaService.getMpaById(newFilm.getMpa().getId());
        }
        if (newFilm.getGenres() != null && !newFilm.getGenres().isEmpty()) {
            for (Genre genre: newFilm.getGenres()) {
                genreService.getGenreById(genre.getId());
            }
        }
        return filmStorage.createFilm(newFilm);
    }

    public Film updateFilm(Film newFilm) {
        if (!filmStorage.existsFilmById(newFilm.getId())) {
            throw new NotFoundException("Фильм не найдем");
        }
        return filmStorage.updateFilm(newFilm);
    }

    public Collection<Film> getTopPopularFilms(int count) {
        return filmStorage.getTopPopularFilms(count);
    }

    public void addLike(Long filmId, Long userId) {
        if (!filmStorage.existsFilmById(filmId)) {
            throw new NotFoundException("Фильм не найден");
        }
        if (!userStorage.existsUserById(userId)) {
            throw new NotFoundException("Пользователь не найден");
        }
        likeService.addLike(filmId, userId);
    }

    public void removeLike(Long filmId, Long userId) {
        if (!filmStorage.existsFilmById(filmId)) {
            throw new NotFoundException("Фильм не найден");
        }
        if (!userStorage.existsUserById(userId)) {
            throw new NotFoundException("Пользователь не найден");
        }
        likeService.removeLike(filmId, userId);
    }
}
