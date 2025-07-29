package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    Film createFilm(Film newFilm);

    Film updateFilm(Film newFilm);

    Optional<Film> getFilmById(long filmId);

    List<Film> getAllFilms();

    List<Film> getTopPopularFilms(int count);

    boolean existsFilmById(long filmId);

}
