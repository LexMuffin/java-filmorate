package ru.yandex.practicum.filmorate.storage;

import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.comparingLong;

@Slf4j
@Component
@RequiredArgsConstructor
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();
    private final Set<String> filmsNamesSet = new HashSet<>();

    @PostConstruct
    public void initNamesSet() {
        films.values().forEach(film -> filmsNamesSet.add(film.getName()));
    }

    @Override
    public Collection<Film> getAllFilms() {
        log.info("GET /films - получение списка всех фильмов");
        return films.values();
    }

    @Override
    public Film getFilmById(long filmId) {
        Film film = films.get(filmId);
        if (film == null) {
            log.error("Фильм с идентификатором - \"{}\" не найден", filmId);
            throw new NotFoundException("Фильм не найден");
        }
        return film;
    }

    @Override
    public Film createFilm(@Valid @RequestBody Film newFilm) {
        log.info("POST /films - добавление нового фильма");
        try {
            if (filmsNamesSet.contains(newFilm.getName())) {
                log.error("Фильм \"{}\" уже существует", newFilm.getName());
                throw new DuplicatedDataException("Фильм с таким названием уже существует");
            }
            newFilm.setId(getNextFilmId());
            films.put(newFilm.getId(), newFilm);
            log.info("Фильм \"{}\" создан", newFilm.getName());
            filmsNamesSet.add(newFilm.getName());
            return newFilm;
        } catch (RuntimeException ex) {
            log.trace("Ошибка - \"{}\" при добавлении нового фильма", ex.getMessage());
            throw ex;
        }
    }

    @Override
    public Film updateFilm(Film newFilm) {
        log.info("PUT /films - обновление существующего фильма");
        try {
            Film existingFilm = films.get(newFilm.getId());
            if (existingFilm == null) {
                log.error("Фильм с идентификатором - \"{}\" не найден", newFilm.getId());
                throw new NotFoundException("Фильм не найден");
            }
            if (filmsNamesSet.contains(newFilm.getName())) {
                log.error("Фильм \"{}\" уже существует", newFilm.getName());
                throw new DuplicatedDataException("Фильм с таким названием уже существует");
            }
            existingFilm.setName(newFilm.getName());
            existingFilm.setDescription(newFilm.getDescription());
            existingFilm.setReleaseDate(newFilm.getReleaseDate());
            existingFilm.setDuration(newFilm.getDuration());
            log.info("Информация о фильме - \"{}\" обновлена", newFilm.getName());
            return existingFilm;
        } catch (RuntimeException ex) {
            log.trace("Ошибка - \"{}\" при обновлении существующего фильма", ex.getMessage());
            throw ex;
        }
    }

    @Override
    public Collection<Film> getTopPopularFilms(int count) {
        return films.values()
                .stream()
                .sorted(Collections.reverseOrder(comparingLong(film -> film.getLikes().size())))
                .limit(count)
                .collect(Collectors.toList());
    }

    private Long getNextFilmId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
