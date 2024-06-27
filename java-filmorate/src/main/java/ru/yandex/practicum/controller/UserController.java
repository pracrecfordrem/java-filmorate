package ru.yandex.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.model.User;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Year;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final HashMap<Long,User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        System.out.println("get request being completed");
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        System.out.println("post being completed");
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        } else if (user.getLogin() == null || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        } else if (user.getName() == null) {
            user.setName(user.getLogin());
        } else if (user.getBirthday().isAfter(LocalDate.of(Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH) ))) {
            throw new ValidationException("Дата рождения не может быть в будущем");
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
    public User update(@RequestBody User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        } else if (user.getLogin() == null || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        } else if (user.getName() == null) {
            user.setName(user.getLogin());
        } else if (user.getBirthday().isAfter(LocalDate.of(Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH) ))) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        } else if (!users.containsKey(user.getId()) || user.getId() == null) {
            throw new ValidationException("ИД изменямого пользователя не может быть равен нулю");
        }
        log.info("Изменён пользователь: " + user);
        users.put(user.getId(), user);
        return user;
    }
}
