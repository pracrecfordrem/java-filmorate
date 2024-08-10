package ru.yandex.practicum.filmorate.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Optional;

@Repository
public class UserDbStorage extends BaseRepository<User> implements UserStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM users";
    private static final String FIND_ONE_QUERY = "SELECT * FROM users where ID = ?";
    private static final String INSERT_QUERY = "INSERT INTO USERS (name, email, login, birthday) values (?, ?, ?, ?)";
    private static final String SELECT_FOR_ID_QUERY = "SELECT ID FROM users where email = ?";
    private static final String UPDATE_QUERY = "UPDATE USERS SET " +
            "NAME = ?, " +
            "EMAIL = ?, " +
            "LOGIN = ?, " +
            "BIRTHDAY = ? " +
            "WHERE ID = ?";
    public UserDbStorage(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Collection<User> findAll() {
        return super.findMany(FIND_ALL_QUERY);
    }

    @Override
    public User create(User user) {
        super.insert(INSERT_QUERY,
                     user.getName(),
                     user.getEmail(),
                     user.getLogin(),
                     user.getBirthday());
        Long id = jdbc.queryForObject(SELECT_FOR_ID_QUERY, Long.class, user.getEmail());
        user.setId(id);
        return user;
    }

    @Override
    public User update(User user) {
        super.update(UPDATE_QUERY,
                    user.getName(),
                    user.getEmail(),
                    user.getLogin(),
                    user.getBirthday(),
                    user.getId());
        return user;
    }

    @Override
    public Optional<User> getUserById(Long userId) {
        return super.findOne(FIND_ONE_QUERY,userId);
    }
}
