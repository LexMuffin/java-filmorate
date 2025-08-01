package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaDbStorageTest {
    private final MpaStorage mpaStorage;

    @Test
    public void getMpaById() {
        Optional<Mpa> mpaOptional = mpaStorage.getMpaById(1);
        assertThat(mpaOptional)
                .isPresent()
                .hasValueSatisfying(mpa -> {
                    assertThat(mpa).hasFieldOrPropertyWithValue("id", 1);
                    assertThat(mpa).hasFieldOrPropertyWithValue("name", "G");
                });
    }

    @Test
    public void getAllMpa() {
        List<Mpa> mpas = mpaStorage.getAllMpa();
        assertThat(mpas).asList().isNotEmpty();
        assertThat(mpas).asList().extracting("name").contains("NC-17");
        assertThat(mpas).asList().extracting("name").contains("PG-13");
    }
}
