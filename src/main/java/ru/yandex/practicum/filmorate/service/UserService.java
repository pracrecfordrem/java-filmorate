package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

@Service
public class UserService {

    private final UserStorage userStorage;

    public UserService(@Autowired UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(Long firstUserLogin, Long secondUserLogin) {
        User first = userStorage.getUserById(firstUserLogin);
        User second = userStorage.getUserById(secondUserLogin);
        if (first == null || second == null) {
            throw new ValidationException("Нельзя добавить в друзья несуществующих пользователей");
        } else {
            first.getFriendsIds().add(secondUserLogin);
            second.getFriendsIds().add(firstUserLogin);
        }
    }
}
