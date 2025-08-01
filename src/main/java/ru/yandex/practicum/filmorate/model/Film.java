package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.filmorate.annotations.MinRealeaseDate;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
@AllArgsConstructor
public class Film {
    private Long id;
    @NotEmpty
    private String name;
    @Size(max = 200)
    private String description;
    @NotNull
    @MinRealeaseDate
    @PastOrPresent
    private LocalDate releaseDate;
    @Positive
    private Integer duration;
    private Mpa mpa;
    private Set<Genre> genres = new HashSet<>();
    private Set<Long> likes = new HashSet<>();
}
