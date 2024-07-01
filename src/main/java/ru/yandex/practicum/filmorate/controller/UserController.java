package ru.yandex.practicum.filmorate.controller;


import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final HashMap<Long,User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody @Valid User user) {
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может содержать пробелы");
        } else if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        if (user.getId() == null) {
            user.setId(getNextId());
        }
        users.put(user.getId(),user);
        log.info("Добавлен пользователь: " + user);
        return user;
    }

    private long getNextId() {
        long currentMaxId = users.size();
        return ++currentMaxId;
    }

    @PutMapping
    public User update(@RequestBody @Valid User user) {
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может содержать пробелы");
        } else if (user.getName() == null) {
            user.setName(user.getLogin());
        } else if (!users.containsKey(user.getId()) || user.getId() == null) {
            throw new ValidationException("ИД изменямого пользователя не может быть равен нулю");
        }
        log.info("Изменён пользователь: " + user);
        users.put(user.getId(), user);
        return user;
    }
}
