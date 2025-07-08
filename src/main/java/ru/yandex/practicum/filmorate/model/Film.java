package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;

import jakarta.validation.constraints.*;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotations.MinRealeaseDate;

@Data
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
}
