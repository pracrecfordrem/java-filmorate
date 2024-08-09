package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Optional;


@Repository
public class FilmDbStorage extends BaseRepository<Film> implements FilmStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM films";
    private static final String INSERT_QUERY = "INSERT INTO films (name, releasedate, mparating, duration, description)" +
            "VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_FOR_ID_QUERY = "SELECT ID FROM FILMS WHERE " +
            "name = ?";
    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Collection<Film> findAll() {
        return super.findMany(FIND_ALL_QUERY);
    }

    @Override
    public Film create(Film film) {
        System.out.println(film.getName() + " " + film.getReleaseDate() + " " + film.getMPArating() + " " + film.getDescription() + " " + film.getDuration());
        insert(
                INSERT_QUERY,
                film.getName(),
                film.getReleaseDate(),
                film.getMPArating(),
                film.getDuration(),
                film.getDescription()
                );
        long id = jdbc.queryForObject(SELECT_FOR_ID_QUERY,Long.class,film.getName());
        film.setId(id);
        return film;
    }

    @Override
    public Film update(Film film) {
        return null;
    }

    @Override
    public Optional<Film> getFilmById(Long filmId) {
        return Optional.empty();
    }
}
