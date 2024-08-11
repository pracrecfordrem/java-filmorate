package ru.yandex.practicum.filmorate.repository.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.MPArating;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MPAratingRowMapper implements RowMapper<MPArating> {
    @Override
    public MPArating mapRow(ResultSet rs, int rowNum) throws SQLException {
        MPArating mpArating = new MPArating();
        mpArating.setId(rs.getLong("id"));
        mpArating.setName(rs.getString("name"));
        return mpArating;
    }
}
