package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> findAll();
    User create(User user);
    User update(User user);
    User findUser(int userId);
    void addAsFriend(int userId, int friendId);
    void removeFromFriends(int userId, int friendId);
    List<User> findUserFriends(int userId);
    List<User> findCommonFriends(int userId, int otherId);
}