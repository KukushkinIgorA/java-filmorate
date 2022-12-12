package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dictionary.UriParam;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.utils.FilmorateUtils;

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
        if(user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userStorage.create(user);
    }

    public User update(User user) {
        validate(user);
        if(user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        final User userUpdate = userStorage.update(user);
        if(userUpdate == null)
            throw new NotFoundException(String.format("на сервере отстутствует пользователь c id = %s", user.getId()));
        return userUpdate;
    }

    public User findUser(String userId) {
        int userIdInt = FilmorateUtils.validateParseInt(userId, UriParam.USER_ID);
        final User user = userStorage.findUser(userIdInt);
        if(user == null)
            throw new NotFoundException(String.format("на сервере отстутствует пользователь c id = %s", userIdInt));
        return user;
    }

    public void addAsFriend(String userId, String friendId) {
        int userIdInt = FilmorateUtils.validateParseInt(userId, UriParam.USER_ID);
        int friendIdInt = FilmorateUtils.validateParseInt(friendId, UriParam.USER_ID);
        User user = userStorage.findUser(userIdInt);
        if (user == null)
            throw new NotFoundException(String.format("на сервере отстутствует пользователь c id = %s", userIdInt));
        User friend = userStorage.findUser(friendIdInt);
        if (friend == null)
            throw new NotFoundException(String.format("на сервере отстутствует пользователь c id = %s", friendIdInt));
        userStorage.addAsFriend(userIdInt, friendIdInt);
    }

    public void removeFromFriends(String userId, String friendId) {
        int userIdInt = FilmorateUtils.validateParseInt(userId, UriParam.USER_ID);
        int friendIdInt = FilmorateUtils.validateParseInt(friendId, UriParam.USER_ID);
        User user = userStorage.findUser(userIdInt);
        if (user == null)
            throw new NotFoundException(String.format("на сервере отстутствует пользователь c id = %s", userIdInt));
        User friend = userStorage.findUser(friendIdInt);
        if (friend == null)
            throw new NotFoundException(String.format("на сервере отстутствует пользователь c id = %s", friendIdInt));
        userStorage.removeFromFriends(userIdInt, friendIdInt);
    }

    public List<User> findUserFriends(String userId) {
        int userIdInt = FilmorateUtils.validateParseInt(userId, UriParam.USER_ID);
        User user = userStorage.findUser(userIdInt);
        if (user == null)
            throw new NotFoundException(String.format("на сервере отстутствует пользователь c id = %s", userIdInt));
        return userStorage.findUserFriends(userIdInt);
    }

    public List<User> findCommonFriends(String userId, String otherId) {
        int userIdInt = FilmorateUtils.validateParseInt(userId, UriParam.USER_ID);
        int otherIdInt = FilmorateUtils.validateParseInt(otherId, UriParam.USER_ID);
        User user = userStorage.findUser(userIdInt);
        if (user == null)
            throw new NotFoundException(String.format("на сервере отстутствует пользователь c id = %s", userIdInt));
        User other = userStorage.findUser(otherIdInt);
        if (other == null)
            throw new NotFoundException(String.format("на сервере отстутствует пользователь c id = %s", otherIdInt));
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
}