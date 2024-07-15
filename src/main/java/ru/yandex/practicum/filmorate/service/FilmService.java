package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Comparator;
import java.util.List;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public FilmService(@Autowired FilmStorage filmStorage, @Autowired UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(Long filmId, Long userId) {
        if (filmStorage.getFilmById(filmId) == null) {
            throw new NotFoundException("Не найден фильм, которому присваивается лайк");
        } else if (userStorage.getUserById(userId) == null) {
            throw new NotFoundException("Не найден пользователь, который ставит лайк");
        } else if (filmId <= 0 || userId <= 0) {
            throw new ValidationException("Некорректный формат переданных параметров");
        } else {
            filmStorage.getFilmById(filmId).getLikes().add(userId);
        }
    }

    public void deleteLike(Long filmId, Long userId) {
        if (filmStorage.getFilmById(filmId) == null) {
            throw new NotFoundException("Не найден фильм, у которого удаляется лайк");
        } else if (userStorage.getUserById(userId) == null) {
            throw new NotFoundException("Не найден пользователь, чей удаляется лайк");
        } else if (filmId <= 0 || userId <= 0) {
            throw new ValidationException("Некорректный формат переданных параметров");
        } else {
            filmStorage.getFilmById(filmId).getLikes().remove(userId);
        }
    }

    public List<Film> getPopularFilms(int count) {
        Comparator<Film> comparator = Comparator.comparing(film -> film.getLikes().size(), Comparator.reverseOrder());
        if (count <= 0) {
            throw new ValidationException("Count должен быть больше 0");
       } else {
            if (count > filmStorage.findAll().size()) {
                return filmStorage.findAll().stream().sorted(comparator).toList();
            }
            List<Film> res = filmStorage.findAll().stream().sorted(comparator).toList();
            return res.subList(0,count - 1);
        }
    }
}
