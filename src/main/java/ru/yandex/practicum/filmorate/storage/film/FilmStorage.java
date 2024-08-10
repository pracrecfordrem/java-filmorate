package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;


import java.util.Collection;
import java.util.Optional;

@Component
public interface FilmStorage {

    public Collection<Film> findAll();

    public Film create(Film film);

    public Film update(Film film);

    public Optional<Film> getFilmById(Long filmId);

    public void addLike(Long filmId, Long userId);

    void deleteLike(Long filmId, Long userId);

    Collection<Film> getPopularFilms(int count);
}
