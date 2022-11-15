package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public User create(User user) {
        validate(user);
        return userStorage.create(user);
    }

    public User update(User user) {
        validate(user);
        return userStorage.update(user);
    }

    public User findUser(String userId) {
        int userIdInt = Integer.parseInt(userId);
        return userStorage.findUser(userIdInt);
    }

    public void addAsFriend(String userId, String friendId) {
        int userIdInt = Integer.parseInt(userId);
        int friendIdInt = Integer.parseInt(friendId);
        userStorage.addAsFriend(userIdInt, friendIdInt);
    }

    public void removeFromFriends(String userId, String friendId) {
        int userIdInt = Integer.parseInt(userId);
        int friendIdInt = Integer.parseInt(friendId);
        userStorage.removeFromFriends(userIdInt, friendIdInt);
    }

    public List<User> findUserFriends(String userId) {
        int userIdInt = Integer.parseInt(userId);
        return userStorage.findUserFriends(userIdInt);
    }

    public List<User> findCommonFriends(String userId, String otherId) {
        int userIdInt = Integer.parseInt(userId);
        int otherIdInt = Integer.parseInt(otherId);
        return userStorage.findCommonFriends(userIdInt, otherIdInt);
    }

    void validate(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !Pattern.matches("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$", user.getEmail())) {
            log.error("электронная почта не может быть пустой и должна содержать символ @ {}", user);
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @");
        } else if (user.getLogin() == null || user.getLogin().isBlank() || Pattern.matches("^.*\\s.*$", user.getLogin())) {
            log.error("логин не может быть пустым и содержать пробелы {}", user);
            throw new ValidationException("логин не может быть пустым и содержать пробелы");
        } else if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            log.error("дата рождения не может быть в будущем {}", user);
            throw new ValidationException("дата рождения не может быть в будущем");
        }
    }
}
