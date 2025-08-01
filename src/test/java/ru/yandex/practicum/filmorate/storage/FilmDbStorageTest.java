package ru.yandex.practicum.filmorate.storage;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FilmDbStorageTest {
    private final FilmStorage filmStorage;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;

    @Test
    @Order(1)
    public void createFilm() {
        Film newFilm = Film.builder()
                .name("test_film")
                .description("test_description")
                .releaseDate(LocalDate.of(1996, 4, 18))
                .duration(100)
                .mpa(mpaStorage.getMpaById(1).get())
                .genres(Set.of(genreStorage.getGenreById(1).get()))
                .build();
        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.createFilm(newFilm));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film -> {
                            assertThat(film).hasFieldOrPropertyWithValue("id", 1L);
                            assertThat(film).hasFieldOrPropertyWithValue("name", "test_film");
                            assertThat(film).hasFieldOrPropertyWithValue("description", "test_description");
                            assertThat(film).hasFieldOrPropertyWithValue("duration", 100);
                            assertThat(film).hasFieldOrPropertyWithValue("mpa", mpaStorage.getMpaById(1).get());
                            assertThat(film).hasFieldOrPropertyWithValue("releaseDate",
                                    LocalDate.of(1996, 4, 18));
                        }
                );
    }

    @Test
    @Order(2)
    public void updateFilm() {
        Film newFilm = Film.builder()
                .id(1L)
                .name("test_film1")
                .description("test_description1")
                .releaseDate(LocalDate.of(1996, 4, 18))
                .duration(100)
                .mpa(mpaStorage.getMpaById(1).get())
                .genres(Set.of(genreStorage.getGenreById(1).get()))
                .build();
        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.updateFilm(newFilm));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film -> {
                            assertThat(film).hasFieldOrPropertyWithValue("id", 1L);
                            assertThat(film).hasFieldOrPropertyWithValue("name", "test_film1");
                            assertThat(film).hasFieldOrPropertyWithValue("description", "test_description1");
                        }
                );

    }

    @Test
    @Order(3)
    public void getFilmById() {
        Optional<Film> filmOptional = filmStorage.getFilmById(1);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film -> {
                            assertThat(film).hasFieldOrPropertyWithValue("id", 1L);
                            assertThat(film).hasFieldOrPropertyWithValue("name", "test_film1");
                            assertThat(film).hasFieldOrPropertyWithValue("description", "test_description1");
                        }
                );
    }

    @Test
    @Order(4)
    public void getAllFilms() {
        Film newFilm = Film.builder()
                .name("test_film2")
                .description("test_description2")
                .releaseDate(LocalDate.of(1997, 5, 19))
                .duration(120)
                .mpa(mpaStorage.getMpaById(2).get())
                .genres(Set.of(genreStorage.getGenreById(2).get()))
                .build();
        filmStorage.createFilm(newFilm);
        Collection<Film> films = filmStorage.getAllFilms();
        assertThat(films).asList().element(0).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(films).asList().element(1).hasFieldOrPropertyWithValue("id", 2L);
        assertThat(films).asList().element(1).hasFieldOrPropertyWithValue("description", "test_description2");
    }

    @Test
    @Order(5)
    public void getTopPopularFilms() {
        Collection<Film> films = filmStorage.getTopPopularFilms(1);
        assertThat(films).asList().element(0).hasFieldOrPropertyWithValue("id", 1L);
    }
}
