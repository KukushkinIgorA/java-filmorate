package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private int nextId = 1;
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping()
    public List<User> findAll() {
        log.info("Запрос всех пользователей. Текущее количество {}", users.size());
        return new ArrayList<>(users.values());
    }

    @PostMapping()
    public User create(@RequestBody User user) {
        log.info("Запрос на создание пользователя {}", user.getLogin());
        validate(user);
        return createUser(user);
    }

    @PutMapping()
    public User update(@RequestBody User user) {
        log.info("Запрос на обновление пользователя {}", user.getId());
        validate(user);
        return updateUser(user);
    }

    void validate(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !Pattern.matches("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$", user.getEmail())) {
            log.error("электронная почта не может быть пустой и должна содержать символ @ {}", user);
            throw new ValidationException(HttpStatus.BAD_REQUEST, "электронная почта не может быть пустой и должна содержать символ @");
        } else if (user.getLogin() == null || user.getLogin().isBlank() || Pattern.matches("^.*\\s.*$", user.getLogin())) {
            log.error("логин не может быть пустым и содержать пробелы {}", user);
            throw new ValidationException(HttpStatus.BAD_REQUEST, "логин не может быть пустым и содержать пробелы");
        } else if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            log.error("дата рождения не может быть в будущем {}", user);
            throw new ValidationException(HttpStatus.BAD_REQUEST, "дата рождения не может быть в будущем");
        }
    }

    User createUser(User user) {
        int id = nextId++;
        user.setId(id);
        if(user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(id, user);
        return users.get(id);
    }

    User updateUser(User user) {
        int id = user.getId();
        if (users.containsKey(id)){
            if(user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            users.put(id, user);
        } else {
            log.error("на сервере отстутствует пользователь для обновления c id = {}", id);
            throw new ValidationException(HttpStatus.NOT_FOUND, "на сервере отстутствует пользователь для обновления");
        }
        return users.get(id);
    }
}
