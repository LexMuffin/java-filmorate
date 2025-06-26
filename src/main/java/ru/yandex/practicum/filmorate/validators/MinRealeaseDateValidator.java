package ru.yandex.practicum.filmorate.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.yandex.practicum.filmorate.annotations.MinRealeaseDate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MinRealeaseDateValidator implements ConstraintValidator<MinRealeaseDate, LocalDate> {

    private LocalDate minDate;

    @Override
    public void initialize(MinRealeaseDate constraintAnnotaition) {
        this.minDate = LocalDate.parse(
                constraintAnnotaition.value(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
        );
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        return value == null || !value.isBefore(minDate);
    }
}
