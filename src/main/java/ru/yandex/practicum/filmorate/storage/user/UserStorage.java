package ru.yandex.practicum.filmorate.storage.user;


import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

@Component
public interface UserStorage {
    public Collection<User> findAll();

    public User create(User user);

    public User update(User user);

    public Optional<User> getUserById(Long userId);

    void addFriend(Long firstUserLogin, Long secondUserLogin);

    Collection<User> getFriendList(Long id);

    void deleteFriend(Long firstUserId, Long secondUserId);

    Collection<User> getCommonFriends(Long firstUserId, Long secondUserId);
}
