package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private Long id;
    private String name;
    @NotEmpty
    @Email
    private String email;
    @NotEmpty
    @Pattern(regexp = "\\S++", message = "Логин не должен содержать пробелы")
    private String login;
    @NotNull
    @PastOrPresent
    private LocalDate birthday;
    private Set<Long> friends = new HashSet<>();
}
