package ru.yandex.practicum.filmorate.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPArating;
import ru.yandex.practicum.filmorate.repository.mappers.MPAratingRowMapper;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;


@Repository
public class FilmDbStorage extends BaseRepository<Film> implements FilmStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM films";
    private static final String FIND_ONE_QUERY = "SELECT * FROM films WHERE ID = ?";
    private static final String INSERT_QUERY = "INSERT INTO films (name, releasedate, mparating_id, duration, description)" +
            "VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_FOR_ID_QUERY = "SELECT MAX(ID) FROM FILMS ";
    private static final String UPDATE_QUERY = "UPDATE FILMS SET " +
            "NAME = ?," +
            "RELEASEDATE = ?, " +
            "MPARATING = ?, " +
            "DURATION = ?, " +
            "DESCRIPTION = ? " +
            "WHERE ID = ?";

    private static final String INSERT_LIKE_QUERY = "INSERT INTO LIKES (FILM_ID, USER_ID, MARK_TIME) " +
            "VALUES(?, ?, ?)";
    private static final String DELETE_LIKE_QUERY = "DELETE FROM LIKES WHERE FILM_ID = ? " +
            "AND USER_ID = ?";
    private static final String FIND_MOST_POPULAR = "WITH prep AS (\n" +
            "             SELECT film_id,\n" +
            "                    count(*) AS cnt\n" +
            "               FROM likes\n" +
            "              GROUP BY film_id\n" +
            "             )\n" +
            "SELECT f.* \n" +
            "  FROM FILMS f\n" +
            "  left JOIN prep\n" +
            "    ON prep.film_id = f.ID \n" +
            " ORDER BY prep.cnt DESC \n" +
            " LIMIT ?\n";
    private static final String FIND_MPA = "SELECT * FROM MPARATING WHERE ID = ?";
    private static final MPAratingRowMapper mPAratingRowMapper = new MPAratingRowMapper();
    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Collection<Film> findAll() {
        return super.findMany(FIND_ALL_QUERY);
    }

    @Override
    public Film create(Film film) {
        Long MPAratingID;
        if (film.getMPArating() == null) {
            MPAratingID = null;
        } else {
            MPAratingID = film.getMPArating().getId();
        };
        super.insert(
                INSERT_QUERY,
                film.getName(),
                film.getReleaseDate(),
                MPAratingID,
                film.getDuration(),
                film.getDescription()
                );

        long id = jdbc.queryForObject(SELECT_FOR_ID_QUERY, Long.class);
        film.setId(id);
        System.out.println(film.getMPArating());
        return film;
    }

    @Override
    public Film update(Film film) {
        super.update(UPDATE_QUERY,
                    film.getName(),
                    film.getReleaseDate(),
                    film.getMPArating().getId(),
                    film.getDuration(),
                    film.getDescription(),
                    film.getId());
        return film;
    }

    @Override
    public Optional<Film> getFilmById(Long filmId) {
        return super.findOne(FIND_ONE_QUERY,filmId);
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        super.insert(INSERT_LIKE_QUERY,filmId,userId, Instant.now());
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        super.delete(DELETE_LIKE_QUERY,filmId,userId);
    }

    @Override
    public Collection<Film> getPopularFilms(int count) {
        return super.findMany(FIND_MOST_POPULAR,count);
    }
}
