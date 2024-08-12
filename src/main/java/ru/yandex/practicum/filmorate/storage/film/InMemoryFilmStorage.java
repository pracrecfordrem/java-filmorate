package ru.yandex.practicum.filmorate.storage.film;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

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
        } else if (film.getId() == null) {
            throw new ValidationException("ИД изменямого фильма не может быть пустым");
        } else if (!films.containsKey(film.getId())) {
            throw new NotFoundException("Не найден изменяемый фильм");
        }
        films.put(film.getId(),film);
        log.info("Изменён фильм: " + film);
        return film;
    }

    @Override
    public Optional<Film> getFilmById(Long filmId) {
        return Optional.ofNullable(films.get(filmId));
    }

    @Override
    public void addLike(Long filmId, Long userId) {

    }

    @Override
    public void deleteLike(Long filmId, Long userId) {

    }

    @Override
    public List<Film> getPopularFilms(int count) {
        return null;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidation(final ValidationException e) {
        return Map.of("error", "Произошла ошибка валидации одного из параметров: " + e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFound(final NotFoundException e) {
        return Map.of("error", "Не найден переданный параметр.");
    }
}
