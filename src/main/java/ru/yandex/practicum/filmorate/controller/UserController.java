package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //Запрос всех пользователей
    @GetMapping()
    public List<User> findAll() {
        log.info("Запрос всех пользователей");
        return userService.findAll();
    }

    //Запрос на создание пользователя
    @PostMapping()
    public User create(@RequestBody User user) {
        log.info("Запрос на создание пользователя {}", user.getLogin());
        return userService.create(user);
    }

    //Запрос на обновление пользователя
    @PutMapping()
    public User update(@RequestBody User user) {
        log.info("Запрос на обновление пользователя {}", user.getId());
        return userService.update(user);
    }

    //Найти данные о пользователе по идентификатору
    @GetMapping("{id}")
    public User findUser (@PathVariable("id") String userId) {
        log.info("Запрос пользователя по id: {}", userId);
        return userService.findUser(userId);
    }

    //добавление в друзья
    @PutMapping("{id}/friends/{friendId}")
    public void addAsFriend(
            @PathVariable("id") String userId,
            @PathVariable() String friendId
    ) {
        log.info("Добавление пользователя {} в друзья пользователя {}", userId, friendId);
        userService.addAsFriend(userId, friendId);
    }

    //удаление из друзей
    @DeleteMapping("{id}/friends/{friendId}")
    public void removeFromFriends(
            @PathVariable("id") String userId,
            @PathVariable() String friendId) {
        log.info("Удаление пользователя {} из друзей пользователя {}", userId, friendId);
        userService.removeFromFriends(userId, friendId);
    }

    //возвращаем список пользователей, являющихся его друзьями
    @GetMapping("{id}/friends")
    public List<User> findUserFriends( @PathVariable("id") String userId) {
        log.info("Запрос друзей пользователя {}", userId);
        return userService.findUserFriends(userId);
    }

    //список друзей, общих с другим пользователем
    @GetMapping("{id}/friends/common/{otherId}")
    public List<User> findCommonFriends(
            @PathVariable("id") String userId,
            @PathVariable() String otherId) {
        log.info("Запрос общих друзей пользователя {} и пользователя {}", userId, otherId);
        return userService.findCommonFriends(userId, otherId);
    }
}
