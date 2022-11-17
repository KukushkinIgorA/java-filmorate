package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage {

    private int nextId = 1;

    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User create(User user) {
        return createUser(user);
    }

    @Override
    public User update(User user) {
        return updateUser(user);
    }

    @Override
    public User findUser(int userId) {
        checkUsersExist(List.of(userId));
        return users.get(userId);
    }

    @Override
    public void addAsFriend(int userId, int friendId) {
        checkUsersExist(List.of(userId, friendId));
        users.get(userId).getFriends().add(friendId);
        users.get(friendId).getFriends().add(userId);
    }

    @Override
    public void removeFromFriends(int userId, int friendId) {
        checkUsersExist(List.of(userId, friendId));
        users.get(userId).getFriends().remove(friendId);
        users.get(friendId).getFriends().remove(userId);
    }

    @Override
    public List<User> findUserFriends(int userId) {
        checkUsersExist(List.of(userId));
        return users.get(userId).getFriends().stream()
                .map(users::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findCommonFriends(int userId, int otherId) {
        checkUsersExist(List.of(userId, otherId));
        return users.get(userId).getFriends().stream()
                .distinct()
                .filter(users.get(otherId).getFriends()::contains)
                .map(users::get)
                .collect(Collectors.toList());
    }

    private User createUser(User user) {
        int id = nextId++;
        user.setId(id);
        if(user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(id, user);
        return users.get(id);
    }

    private User updateUser(User user) {
        int id = user.getId();
        checkUsersExist(List.of(id));
        if(user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(id, user);
        return users.get(id);
    }

    private void checkUsersExist(List<Integer> userIds) {
        for (Integer userId : userIds) {
            if (!users.containsKey(userId))
                throw new NotFoundException(String.format("на сервере отстутствует пользователь c id = %s", userId));
        }
    }
}
