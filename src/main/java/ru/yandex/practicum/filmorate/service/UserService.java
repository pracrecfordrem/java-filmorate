package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final UserStorage userStorage;

    public UserService(@Autowired UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addFriend(Long firstUserLogin, Long secondUserLogin) {
        Optional<User> first = userStorage.getUserById(firstUserLogin);
        Optional<User> second = userStorage.getUserById(secondUserLogin);
        if (first.isEmpty() || second.isEmpty()) {
            throw new NotFoundException("Нельзя добавить в друзья несуществующих пользователей");
        } else if (firstUserLogin <= 0 || secondUserLogin <= 0) {
            throw new ValidationException(firstUserLogin + " " + secondUserLogin);
        } else {
            first.get().getFriendsIds().add(secondUserLogin);
            second.get().getFriendsIds().add(firstUserLogin);
            return first.get();
        }
    }

    public void deleteFriend(Long firstUserLogin, Long secondUserLogin) {
        Optional<User> first = userStorage.getUserById(firstUserLogin);
        Optional<User> second = userStorage.getUserById(secondUserLogin);
        if (first.isEmpty() || second.isEmpty()) {
            throw new NotFoundException("Нельзя удалить из друзей несуществующих пользователей");
        } else if (firstUserLogin <= 0 || secondUserLogin <= 0) {
            throw new ValidationException(firstUserLogin + " " + secondUserLogin);
        } else {
            first.get().getFriendsIds().remove(secondUserLogin);
            second.get().getFriendsIds().remove(firstUserLogin);
        }
    }

    public Collection<User> getFriendList(Long id) {
        Optional<User> searchedUser = userStorage.getUserById(id);
         if (searchedUser.isEmpty()) {
             throw new NotFoundException("Запрошен список друзей у несуществующего пользователя");
         }
         Collection<User> res = new HashSet<User>();
        for (Long friendId: searchedUser.get().getFriendsIds()) {
            res.add(userStorage.getUserById(friendId).get());
        }
        return res;
    }

    public Collection<User> getCommonFriendList(Long firstUserLogin, Long secondUserLogin) {
        Optional<User> first = userStorage.getUserById(firstUserLogin);
        Optional<User> second = userStorage.getUserById(secondUserLogin);
        if (first.isEmpty() || second.isEmpty()) {
            throw new NotFoundException("Нельзя найти список общих друзей у несуществующих пользователей");
        } else if (firstUserLogin <= 0 || secondUserLogin <= 0) {
            throw new ValidationException(firstUserLogin + " " + secondUserLogin);
        } else {
            Set<Long> result = userStorage.getUserById(firstUserLogin).get().getFriendsIds();
            result.retainAll(userStorage.getUserById(secondUserLogin).get().getFriendsIds());
            Collection<User> res = new HashSet<User>();
            for (Long friendId: result) {
                res.add(userStorage.getUserById(friendId).get());
            }
            return res;
        }
    }

    public Optional<User> findOne(Long userId) {
        return userStorage.getUserById(userId);
    }

    public User updateUser(User user) {
        Optional<User> updatedUser = findOne(user.getId());
        if (updatedUser.isEmpty()) {
            throw new NotFoundException("Пользователь не найден");
        } else {
            userStorage.update(user);
        }
        return user;
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

}
