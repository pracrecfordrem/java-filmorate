package ru.yandex.practicum.filmorate.repository.mappers;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPArating;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Component
public class FilmRowMapper implements RowMapper<Film> {
    private static final MPAratingRowMapper mparatingRowMapper = new MPAratingRowMapper();
    private static final String FIND_MPA_RATING = "SELECT * FROM MPArating where id = ?";
    protected final JdbcTemplate jdbc;

    public FilmRowMapper(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getLong("id"));
        film.setName(rs.getString("name"));
        film.setMPArating(jdbc.queryForObject(FIND_MPA_RATING,mparatingRowMapper,rs.getLong("MPArating_id")));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("releaseDate").toLocalDate());
        film.setDuration(rs.getLong("duration"));
        //System.out.println("mpa:" + film.getMPArating());
        return film;
    }
}