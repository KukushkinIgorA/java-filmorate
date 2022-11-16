package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dictionary.UriParam;
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
        int userIdInt = validateParseInt(userId);
        return userStorage.findUser(userIdInt);
    }

    public void addAsFriend(String userId, String friendId) {
        int userIdInt = validateParseInt(userId);
        int friendIdInt = validateParseInt(friendId);
        userStorage.addAsFriend(userIdInt, friendIdInt);
    }

    public void removeFromFriends(String userId, String friendId) {
        int userIdInt = validateParseInt(userId);
        int friendIdInt = validateParseInt(friendId);
        userStorage.removeFromFriends(userIdInt, friendIdInt);
    }

    public List<User> findUserFriends(String userId) {
        int userIdInt = validateParseInt(userId);
        return userStorage.findUserFriends(userIdInt);
    }

    public List<User> findCommonFriends(String userId, String otherId) {
        int userIdInt = validateParseInt(userId);
        int otherIdInt = validateParseInt(otherId);
        return userStorage.findCommonFriends(userIdInt, otherIdInt);
    }

    void validate(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !Pattern.matches("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$", user.getEmail())) {
            throw new ValidationException(String.format("электронная почта не может быть пустой и должна содержать символ @: %s", user.getEmail()));
        } else if (user.getLogin() == null || user.getLogin().isBlank() || Pattern.matches("^.*\\s.*$", user.getLogin())) {
            throw new ValidationException(String.format("логин не может быть пустым и содержать пробелы: %s", user.getLogin()));
        } else if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException(String.format("дата рождения не может быть в будущем: %s", user.getBirthday()));
        }
    }

    private int validateParseInt(String value) {
        Pattern pattern = Pattern.compile("^-?\\d+$");
        if (value == null || !pattern.matcher(value).matches()) {
            throw new ValidationException(String.format("%s не валидное: %s", UriParam.USER_ID.getLabel(), value));
        } else {
            return Integer.parseInt(value);
        }
    }
}