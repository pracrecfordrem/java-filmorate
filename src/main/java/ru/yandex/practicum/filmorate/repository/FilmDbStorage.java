package ru.yandex.practicum.filmorate.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.Optional;


@Repository
public class FilmDbStorage extends BaseRepository<Film> implements FilmStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM films";
    private static final String FIND_ONE_QUERY = "SELECT * FROM films WHERE ID = ?";
    private static final String INSERT_QUERY = "INSERT INTO films (name, releasedate, mparating, duration, genre, description)" +
            "VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_FOR_ID_QUERY = "SELECT ID FROM FILMS WHERE " +
            "NAME = ? " +
            "AND RELEASEDATE = ? " +
            "AND MPARATING = ? " +
            "AND DURATION = ? " +
            "AND GENRE = ? " +
            "AND DESCRIPTION = ? ";
    private static final String UPDATE_QUERY = "UPDATE FILMS SET " +
            "NAME = ?," +
            "RELEASEDATE = ?, " +
            "MPARATING = ?, " +
            "DURATION = ?, " +
            "GENRE = ?, " +
            "DESCRIPTION = ? " +
            "WHERE ID = ?";
    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Collection<Film> findAll() {
        return super.findMany(FIND_ALL_QUERY);
    }

    @Override
    public Film create(Film film) {
        super.insert(
                INSERT_QUERY,
                film.getName(),
                film.getReleaseDate(),
                film.getMPArating(),
                film.getDuration(),
                film.getGenre(),
                film.getDescription()
                );
        long id = jdbc.queryForObject(SELECT_FOR_ID_QUERY,Long.class,
                film.getName(),
                film.getReleaseDate(),
                film.getMPArating(),
                film.getDuration(),
                film.getGenre(),
                film.getDescription());
        film.setId(id);
        return film;
    }

    @Override
    public Film update(Film film) {
        super.update(UPDATE_QUERY,
                    film.getName(),
                    film.getReleaseDate(),
                    film.getMPArating(),
                    film.getDuration(),
                    film.getGenre(),
                    film.getDescription(),
                    film.getId());
        return film;
    }

    @Override
    public Optional<Film> getFilmById(Long filmId) {
        return super.findOne(FIND_ONE_QUERY,filmId);
    }
}
