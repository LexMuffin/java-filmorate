package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getFilms() {
        log.info("GET /films - получение списка всех фильмов");
        return films.values();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film newFilm) {
        log.info("POST /films - добавление нового фильма");
        try {
            if (films.values().stream().anyMatch(f -> f.getName().equalsIgnoreCase(newFilm.getName()))) {
                log.error("Фильм \"{}\" уже существует", newFilm.getName());
                throw new DuplicatedDataException("Фильм с таким названием уже существует");
            }
            newFilm.setId(getNextFilmId());
            films.put(newFilm.getId(), newFilm);
            log.info("Фильм \"{}\" создан", newFilm.getName());
            return newFilm;
        } catch (RuntimeException ex) {
            log.trace("Ошибка - \"{}\" при добавлении нового фильма", ex.getMessage());
            throw ex;
        }
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film newFilm) {
        log.info("PUT /films - обновление существующего фильма");
        try {
            Film existingFilm = films.get(newFilm.getId());
            if (existingFilm == null) {
                log.error("Фильм с идентификатором - \"{}\" не найден", newFilm.getId());
                throw new NotFoundException("Фильм не найден");
            }
            if (films.values().stream().anyMatch(f -> f.getName().equalsIgnoreCase(newFilm.getName()))) {
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

    private Long getNextFilmId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
