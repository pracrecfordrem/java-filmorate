package ru.yandex.practicum.filmorate.service;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public FilmService(@Autowired FilmStorage filmStorage, @Autowired UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Optional<Film> findOne(Long filmId) {
        return filmStorage.getFilmById(filmId);
    }

    public Film create(Film film) {
        if (film.getReleaseDate().isBefore(Film.MIN_DATE)) {
            throw new ValidationException("Дата фильма не может быть ранее 28 декабря 1895 года.");
        }
        filmStorage.create(film);
        return film;
    }

    public void addLike(Long filmId, Long userId) {
        Optional<Film> film = filmStorage.getFilmById(filmId);
        if (film.isEmpty()) {
            throw new NotFoundException("Не найден фильм, которому присваивается лайк");
        } else if (userStorage.getUserById(userId).isEmpty()) {
            throw new NotFoundException("Не найден пользователь, который ставит лайк");
        } else if (filmId <= 0 || userId <= 0) {
            throw new ValidationException("Некорректный формат переданных параметров");
        } else {
            filmStorage.addLike(filmId, userId);
        }
    }

    public void deleteLike(Long filmId, Long userId) {
        Optional<Film> film = filmStorage.getFilmById(filmId);
        if (film.isEmpty()) {
            throw new NotFoundException("Не найден фильм, у которого удаляется лайк");
        } else if (userStorage.getUserById(userId).isEmpty()) {
            throw new NotFoundException("Не найден пользователь, чей удаляется лайк");
        } else if (filmId <= 0 || userId <= 0) {
            throw new ValidationException("Некорректный формат переданных параметров");
        } else {
            filmStorage.deleteLike(filmId,userId);
        }
    }

    public Collection<Film> getPopularFilms(int count) {
        if (count <= 0) {
            throw new ValidationException("Count должен быть больше 0");
        } else {
            if (count > filmStorage.findAll().size()) {
                return filmStorage.findAll();
            }
            return filmStorage.getPopularFilms(count);
        }
    }

    public Film updateFilm(Film film) {
        Optional<Film> updatedFilm = findOne(film.getId());
        if (updatedFilm.isEmpty()) {
            throw new NotFoundException("Фильм не найден");
        } else {
            filmStorage.update(film);
        }
        return film;
    }
}
