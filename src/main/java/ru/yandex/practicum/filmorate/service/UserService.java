package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Service
public class UserService {

    private final UserStorage userStorage;

    public UserService(@Autowired UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addFriend(Long firstUserId, Long secondUserId) {
        Optional<User> first = userStorage.getUserById(firstUserId);
        Optional<User> second = userStorage.getUserById(secondUserId);
        if (first.isEmpty() || second.isEmpty()) {
            throw new NotFoundException("Нельзя добавить в друзья несуществующих пользователей");
        } else if (firstUserId <= 0 || secondUserId <= 0) {
            throw new ValidationException(firstUserId + " " + secondUserId);
        } else {
            userStorage.addFriend(firstUserId,secondUserId);
            return first.get();
        }
    }

    public void deleteFriend(Long firstUserId, Long secondUserId) {
        Optional<User> first = userStorage.getUserById(firstUserId);
        Optional<User> second = userStorage.getUserById(secondUserId);
        if (first.isEmpty() || second.isEmpty()) {
            throw new NotFoundException("Нельзя удалить из друзей несуществующих пользователей");
        } else if (firstUserId <= 0 || secondUserId <= 0) {
            throw new ValidationException(firstUserId + " " + secondUserId);
        } else {
            first.get().getFriendsIds().remove(secondUserId);
            second.get().getFriendsIds().remove(firstUserId);
            userStorage.deleteFriend(firstUserId, secondUserId);
        }
    }

    public Collection<User> getFriendList(Long id) {
        Optional<User> searchedUser = userStorage.getUserById(id);
         if (searchedUser.isEmpty()) {
             throw new NotFoundException("Запрошен список друзей у несуществующего пользователя");
         }
        return userStorage.getFriendList(id);
    }

    public Collection<User> getCommonFriendList(Long firstUserId, Long secondUserId) {
        Optional<User> first = userStorage.getUserById(firstUserId);
        Optional<User> second = userStorage.getUserById(secondUserId);
        if (first.isEmpty() || second.isEmpty()) {
            throw new NotFoundException("Нельзя найти список общих друзей у несуществующих пользователей");
        } else if (firstUserId <= 0 || secondUserId <= 0) {
            throw new ValidationException(firstUserId + " " + secondUserId);
        } else {
            return userStorage.getCommonFriends(firstUserId,secondUserId);
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
