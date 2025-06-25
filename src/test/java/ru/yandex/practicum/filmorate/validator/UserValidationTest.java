package ru.yandex.practicum.filmorate.validator;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class UserValidationTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testUserInvalidEmail() {
        User user = new User();
        user.setId(1L);
        user.setName("testName");
        user.setEmail("test_email");
        user.setLogin("testLogin");
        user.setBirthday(LocalDate.parse("2000-01-01"));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals("должно иметь формат адреса электронной почты", violations.iterator().next().getMessage());
    }

    @Test
    public void testUserLoginWhitespace() {
        User user = new User();
        user.setId(1L);
        user.setName("testName");
        user.setEmail("test@email");
        user.setLogin("test Login");
        user.setBirthday(LocalDate.parse("2000-01-01"));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals("Логин не должен содержать пробелы", violations.iterator().next().getMessage());
    }

    @Test
    public void testUserBirthdayFuture() {
        User user = new User();
        user.setId(1L);
        user.setName("testName");
        user.setEmail("test@email");
        user.setLogin("testLogin");
        user.setBirthday(LocalDate.parse("2030-01-01"));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals("должно содержать прошедшую дату или сегодняшнее число", violations.iterator().next().getMessage());
    }
}
