package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.impl.InMemoryUserStorage;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    UserService userService;
    User user;

    @BeforeEach
    void init() {
        userService = new UserService(new InMemoryUserStorage());
        user = new User(1, "garyn@mail.ru", "Garyn", "Игорь", LocalDate.of(1990, Month.JUNE, 18), null);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"garynmail.ru", "garyn@mailru"})
    void validateEmail(String email) {
        assertDoesNotThrow(() -> userService.validate(user));
        user.setEmail(email);
        assertThrows(ValidationException.class, () -> userService.validate(user));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"Gar yn", " Garyn "})
    void validateLogin(String login) {
        assertDoesNotThrow(() -> userService.validate(user));
        user.setLogin(login);
        assertThrows(ValidationException.class, () -> userService.validate(user));
    }

    @Test
    void validateBirthday() {
        assertDoesNotThrow(() -> userService.validate(user));
        user.setBirthday(null);
        assertDoesNotThrow(() -> userService.validate(user));
        user.setBirthday(LocalDate.now().plusDays(1));
        assertThrows(ValidationException.class, () -> userService.validate(user));
    }
}