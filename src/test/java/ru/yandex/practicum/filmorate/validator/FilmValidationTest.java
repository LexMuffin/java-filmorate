package ru.yandex.practicum.filmorate.validator;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class FilmValidationTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testFilmNameEmpty() {
        Film film = new Film();
        film.setId(1L);
        film.setName("");
        film.setDescription("testDescription");
        film.setReleaseDate(LocalDate.parse("1896-12-28"));
        film.setDuration(100);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertEquals("не должно быть пустым", violations.iterator().next().getMessage());
    }

    @Test
    public void testFilmDescriptionMaxLength() {
        Film film = new Film();
        film.setId(1L);
        film.setName("testName");
        film.setDescription("testDescription".repeat(14));
        film.setReleaseDate(LocalDate.parse("1896-12-28"));
        film.setDuration(100);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertEquals("размер должен находиться в диапазоне от 0 до 200", violations.iterator().next().getMessage());
    }

    @Test
    public void testFilmMinReleaseDate() {
        Film film = new Film();
        film.setId(1L);
        film.setName("testName");
        film.setDescription("testDescription");
        film.setReleaseDate(LocalDate.parse("1894-12-28"));
        film.setDuration(100);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertEquals("дата релиза — не раньше 28 декабря 1895 года", violations.iterator().next().getMessage());
    }

    @Test
    public void testFilmNegativeDuration() {
        Film film = new Film();
        film.setId(1L);
        film.setName("testName");
        film.setDescription("testDescription");
        film.setReleaseDate(LocalDate.parse("1896-12-28"));
        film.setDuration(-100);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertEquals("должно быть больше 0", violations.iterator().next().getMessage());
    }


}
