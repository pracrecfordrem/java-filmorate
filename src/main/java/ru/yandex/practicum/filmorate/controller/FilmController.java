package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.MPAService;

import java.util.*;


@RestController
@RequestMapping("/films")
@Qualifier("FilmDbStorage")
public class FilmController {

    private final FilmService filmService;
    private final MPAService mpaService;
    private final GenreService genreService;

    public FilmController(@Autowired FilmService filmService, MPAService mpaService, GenreService genreService) {
        this.filmService = filmService;
        this.mpaService = mpaService;
        this.genreService = genreService;
    }


    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.addLike(id,userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.deleteLike(id,userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopularFilms(@PathVariable("count") @RequestParam(defaultValue = "10") int count) {
        return filmService.getPopularFilms(count);
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

    @PostMapping
    public Film create(@RequestBody @Valid Film film) {
        if (mpaService.findOne(film.getMpa().getId()).isEmpty()) {
            throw new ValidationException("Не найден MPA с рейтингом" + film.getMpa().getId());
        } else if (!checkGenres(film)) {
            throw new ValidationException("Не найден один из переданных жанров");
        }
        return filmService.create(film);
    }

    @GetMapping
    public Collection<Film> findAll() {
        return filmService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Film> findOne(@PathVariable Long id) {
        return filmService.findOne(id);
    }

    @PutMapping
    public Film update(@RequestBody @Valid Film film) {
        return filmService.updateFilm(film);
    }

    boolean checkGenres(Film film) {
        HashSet<Genre> genres = film.getGenres();
        if (genres == null) {
            return true;
        }
        for (Genre genre: genres) {
            if (genreService.findOne(genre.getId()).isEmpty()) {
                return false;
            }
        }
        return true;
    }

}
