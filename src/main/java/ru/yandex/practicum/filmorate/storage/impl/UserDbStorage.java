package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserFriend;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Repository("userDbStorage")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> findAll() {
        String sqlQuery = "select USER_ID, EMAIL, LOGIN, NAME, BIRTHDAY "+
                "from USERS";
        final List<User> users = jdbcTemplate.query(sqlQuery, UserDbStorage::makeUser);
        return users;
    }

    @Override
    public User create(User user) {
        String sqlQuery = "insert into USERS (EMAIL, LOGIN, NAME, BIRTHDAY) values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement stmt = con.prepareStatement(sqlQuery, new String[]{"USER_ID"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            final LocalDate birthdate = user.getBirthday();
            if (birthdate == null){
                stmt.setNull(4, Types.DATE);
            } else {
                stmt.setDate(4, Date.valueOf(birthdate));
            }
            return stmt;
        }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return user;
    }

    @Override
    public User update(User user) {
        String sqlUpdate = "update USERS " +
                "set EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ? " +
                "where USER_ID = ?";
        int updateRecordCount = jdbcTemplate.update(sqlUpdate, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        if (updateRecordCount != 1) {
            return null;
        } else {
            return user;
        }
    }

    @Override
    public User findUser(int userId) {
        String sqlQuery = "select USER_ID, EMAIL, LOGIN, NAME, BIRTHDAY "+
                "from USERS "+
                "where USER_ID = ?";
        final List<User> users = jdbcTemplate.query(sqlQuery, UserDbStorage::makeUser, userId);
        if(users.size() == 0){
            return null;
        } else {
            return users.get(0);
        }
    }

    @Override
    public void addAsFriend(int userId, int friendId) {
        String sqlQuery = "select USER_FRIEND_ID, USER_ID, FRIEND_ID, CONFIRM_FLG " +
                "from USER_FRIEND " +
                "where (USER_ID = ? AND FRIEND_ID = ?) OR " +
                "(USER_ID = ? AND FRIEND_ID = ?)";
        String sqlInsert = "insert into USER_FRIEND (USER_ID, FRIEND_ID) values (?, ?)";
        String sqlUpdate = "update USER_FRIEND " +
                "set CONFIRM_FLG = 'Y' " +
                "where USER_ID = ? AND FRIEND_ID = ?";
        final List<UserFriend> userFriends = jdbcTemplate.query(sqlQuery, UserDbStorage::makeUserFriend, userId, friendId, friendId, userId);
        if(userFriends.size() == 0){
            jdbcTemplate.update(sqlInsert, userId, friendId);
        } else if(userFriends.get(0).getFriendId() == userId && !userFriends.get(0).isConfirmFlg()){
            jdbcTemplate.update(sqlUpdate, friendId, userId);
        }
    }

    @Override
    public void removeFromFriends(int userId, int friendId) {
        String sqlQuery = "select USER_FRIEND_ID, USER_ID, FRIEND_ID, CONFIRM_FLG " +
                "from USER_FRIEND " +
                "where (USER_ID = ? AND FRIEND_ID = ?) OR " +
                "(USER_ID = ? AND FRIEND_ID = ?)";
        String sqlDelete = "delete " +
                "from USER_FRIEND " +
                "where USER_ID = ? AND FRIEND_ID = ?";
        String sqlUpdate = "update USER_FRIEND " +
                "set CONFIRM_FLG = 'N', USER_ID = ?, FRIEND_ID = ?" +
                "where USER_ID = ? AND FRIEND_ID = ?";
        final List<UserFriend> userFriends = jdbcTemplate.query(sqlQuery, UserDbStorage::makeUserFriend, userId, friendId, friendId, userId);
        if(userFriends.size() == 0) {
        } else if(userFriends.get(0).getUserId() == userId && !userFriends.get(0).isConfirmFlg()){
            jdbcTemplate.update(sqlDelete, userId, friendId);
        } else if(userFriends.get(0).getUserId() == userId && userFriends.get(0).isConfirmFlg()){
            jdbcTemplate.update(sqlUpdate, friendId, userId, userId, friendId);
        } else if (userFriends.get(0).getFriendId() == userId && userFriends.get(0).isConfirmFlg()){
            jdbcTemplate.update(sqlUpdate, friendId, userId, friendId, userId);
        }
    }

    @Override
    public List<User> findUserFriends(int userId) {
        String sqlQuery = "select u.USER_ID, u.EMAIL, u.LOGIN, u.NAME, u.BIRTHDAY " +
                "from USER_FRIEND uf join USERS u on uf.USER_ID = u.USER_ID  " +
                "where FRIEND_ID = ? " +
                "UNION ALL " +
                "select u.USER_ID, u.EMAIL, u.LOGIN, u.NAME, u.BIRTHDAY " +
                "from USER_FRIEND uf join USERS u on uf.FRIEND_ID= u.USER_ID " +
                "where uf.USER_ID = ? --and CONFIRM_FLG = true";
        final List<User> users = jdbcTemplate.query(sqlQuery, UserDbStorage::makeUser, userId, userId);
        return users;
    }

    @Override
    public List<User> findCommonFriends(int userId, int otherId) {
        String sqlQuery = "select u.USER_ID, u.EMAIL, u.LOGIN, u.NAME, u.BIRTHDAY " +
                "from USERS u " +
                "where u.USER_ID in ( " +
                "select uf.USER_ID " +
                "from USER_FRIEND uf " +
                "where FRIEND_ID = ? " +
                "union all " +
                "select uf.FRIEND_ID " +
                "from USER_FRIEND uf " +
                "where uf.USER_ID = ? and CONFIRM_FLG = true) " +
                "INTERSECT " +
                "select o.USER_ID, o.EMAIL, o.LOGIN, o.NAME, o.BIRTHDAY " +
                "from USERS o " +
                "where o.USER_ID in ( " +
                "select uf.USER_ID " +
                "from USER_FRIEND uf " +
                "where FRIEND_ID = ? " +
                "union all " +
                "select uf.FRIEND_ID " +
                "from USER_FRIEND uf " +
                "where uf.USER_ID = ? and CONFIRM_FLG = true)";
        final List<User> users = jdbcTemplate.query(sqlQuery, UserDbStorage::makeUser, userId, userId, otherId, otherId);
        return users;
    }

    static User makeUser(ResultSet rs, int rowNum) throws SQLException {
        return new User(
                rs.getInt("USER_ID"),
                rs.getString("EMAIL"),
                rs.getString("LOGIN"),
                rs.getString("NAME"),
                rs.getDate("BIRTHDAY").toLocalDate()
        );
    }

    static UserFriend makeUserFriend(ResultSet rs, int rowNum) throws SQLException {
        return new UserFriend(
                rs.getInt("USER_FRIEND_ID"),
                rs.getInt("USER_ID"),
                rs.getInt("FRIEND_ID"),
                rs.getBoolean("CONFIRM_FLG")
        );
    }
}
