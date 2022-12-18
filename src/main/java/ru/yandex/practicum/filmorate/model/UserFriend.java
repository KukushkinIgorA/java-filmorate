package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserFriend {
    int userId;
    int friendId;
    boolean confirmFlg;
}
