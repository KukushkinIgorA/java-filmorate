package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserControllerTest {
    UserController userController;
    User user;

    @BeforeEach
    void init() {
        userController = new UserController();
        user = new User(1, "garyn@mail.ru", "Garyn", "Игорь", LocalDate.of(1990, Month.JUNE, 18));
    }

    @Test
    void validateEmail() {
        assertDoesNotThrow(() -> userController.validate(user));
        user.setEmail("");
        assertThrows(ValidationException.class, () -> userController.validate(user));
        user.setEmail(null);
        assertThrows(ValidationException.class, () -> userController.validate(user));
        user.setEmail("garynmail.ru");
        assertThrows(ValidationException.class, () -> userController.validate(user));
    }

    @Test
    void validateLogin() {
        assertDoesNotThrow(() -> userController.validate(user));
        user.setLogin("");
        assertThrows(ValidationException.class, () -> userController.validate(user));
        user.setLogin(null);
        assertThrows(ValidationException.class, () -> userController.validate(user));
        user.setLogin("Gar yn");
        assertThrows(ValidationException.class, () -> userController.validate(user));
    }

    @Test
    void validateBirthday() {
        assertDoesNotThrow(() -> userController.validate(user));
        user.setBirthday(null);
        assertDoesNotThrow(() -> userController.validate(user));
        user.setBirthday(LocalDate.now().plusDays(1));
        assertThrows(ValidationException.class, () -> userController.validate(user));
    }
}