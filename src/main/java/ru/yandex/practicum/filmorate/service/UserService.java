package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    private final UserStorage userStorage;

    public UserService(@Autowired UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addFriend(Long firstUserLogin, Long secondUserLogin) {
        User first = userStorage.getUserById(firstUserLogin);
        User second = userStorage.getUserById(secondUserLogin);
        if (first == null || second == null) {
            throw new NotFoundException("Нельзя добавить в друзья несуществующих пользователей");
        } else if (firstUserLogin <= 0 || secondUserLogin <= 0) {
            throw new ValidationException(firstUserLogin + " " + secondUserLogin);
        } else {
            first.getFriendsIds().add(secondUserLogin);
            second.getFriendsIds().add(firstUserLogin);
            return first;
        }
    }

    public void deleteFriend(Long firstUserLogin, Long secondUserLogin) {
        User first = userStorage.getUserById(firstUserLogin);
        User second = userStorage.getUserById(secondUserLogin);
        if (first == null || second == null) {
            throw new NotFoundException("Нельзя удалить из друзей несуществующих пользователей");
        } else if (firstUserLogin <= 0 || secondUserLogin <= 0) {
            throw new ValidationException(firstUserLogin + " " + secondUserLogin);
        } else {
            first.getFriendsIds().remove(secondUserLogin);
            second.getFriendsIds().remove(firstUserLogin);
        }
    }

    public Collection<User> getFriendList(Long id) {
        User searchedUser = userStorage.getUserById(id);
         if (searchedUser == null) {
             throw new NotFoundException("Запрошен список друзей у несуществующего пользователя");
         }
         Collection<User> res = new HashSet<User>();
        for (Long friendId: searchedUser.getFriendsIds()) {
            res.add(userStorage.getUserById(friendId));
        }
        return res;
    }

    public Collection<User> getCommonFriendList(Long firstUserLogin, Long secondUserLogin) {
        User first = userStorage.getUserById(firstUserLogin);
        User second = userStorage.getUserById(secondUserLogin);
        if (first == null || second == null) {
            throw new NotFoundException("Нельзя найти список общих друзей у несуществующих пользователей");
        } else if (firstUserLogin <= 0 || secondUserLogin <= 0) {
            throw new ValidationException(firstUserLogin + " " + secondUserLogin);
        } else {
            Set<Long> result = userStorage.getUserById(firstUserLogin).getFriendsIds();
            result.retainAll(userStorage.getUserById(secondUserLogin).getFriendsIds());
            Collection<User> res = new HashSet<User>();
            for (Long friendId: result) {
                res.add(userStorage.getUserById(friendId));
            }
            return res;
        }
    }

}
