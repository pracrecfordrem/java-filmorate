package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;


import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();
    private static final LocalDate MIN_DATE = LocalDate.of(1895,12,28);

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody @Valid Film film) {
        if (film.getReleaseDate().isBefore(MIN_DATE)) {
            throw new ValidationException("Дата фильма не может быть ранее 28 декабря 1895 года.");
        }
        if (film.getId() == null) {
            film.setId(getNextId());
        }
        films.put(film.getId(),film);
        log.info("Добавлен фильм: " + film);
        return film;
    }

    private long getNextId() {
        long currentMaxId = films.size();
        return ++currentMaxId;
    }


    @PutMapping
    public Film update(@RequestBody @Valid Film film) {
        if (film.getReleaseDate().isBefore(MIN_DATE)) {
            throw new ValidationException("Дата фильма не может быть ранее 28 декабря 1895 года.");
        } else if (!films.containsKey(film.getId()) || film.getId() == null) {
            throw new ValidationException("ИД изменямого фильма не может быть пустым");
        }
        films.put(film.getId(),film);
        log.info("Изменён фильм: " + film);
        return film;
    }


}
