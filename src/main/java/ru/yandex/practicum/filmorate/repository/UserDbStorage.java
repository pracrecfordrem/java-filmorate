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
    private static final String INSERT_USER_QUERY = "INSERT INTO USERS (name, email, login, birthday)" +
            " values (?, ?, ?, ?)";
    private static final String SELECT_FOR_ID_QUERY = "SELECT ID FROM users where email = ?";
    private static final String UPDATE_QUERY = "UPDATE USERS SET " +
            "NAME = ?, " +
            "EMAIL = ?, " +
            "LOGIN = ?, " +
            "BIRTHDAY = ? " +
            "WHERE ID = ?";
    private static final String INSERT_FRIEND_QUERY = "INSERT INTO FRIENDSHIP (INITIATOR_ID, RECIPIENT_ID) " +
            "VALUES(?, ?)";
    private static final String SELECT_FRIENDS_QUERY = "SELECT * FROM USERS " +
            "WHERE ID in " +
            "(SELECT RECIPIENT_ID FROM FRIENDSHIP " +
            "where INITIATOR_ID = ?) ";
    private static final String DELETE_FRIEND_QUERY = "DELETE FROM FRIENDSHIP " +
            "WHERE INITIATOR_ID = ? " +
            "  AND RECIPIENT_ID = ? ";
    private static final String SELECT_COMMON_FRIENDS = "WITH one AS (\n" +
            "             SELECT recipient_id\n" +
            "               FROM FRIENDSHIP\n" +
            "              WHERE initiator_id = ?\n" +
            "            ),\n" +
            "     two AS (\n" +
            "             SELECT recipient_id\n" +
            "               FROM FRIENDSHIP\n" +
            "              WHERE initiator_id = ?\n" +
            "            )\n" +
            "   SELECT u.* \n" +
            "     FROM users u\n" +
            "    INNER JOIN one\n" +
            "       ON one.recipient_id = u.ID \n" +
            "    INNER JOIN two\n" +
            "       ON two.recipient_id = u.id\n";

    public UserDbStorage(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Collection<User> findAll() {
        return super.findMany(FIND_ALL_QUERY);
    }

    @Override
    public User create(User user) {
        super.insert(INSERT_USER_QUERY,
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

    @Override
    public void addFriend(Long firstUserId, Long secondUserId) {
        super.insert(INSERT_FRIEND_QUERY,
                firstUserId,
                secondUserId);
    }

    @Override
    public Collection<User> getFriendList(Long id) {
        return super.findMany(SELECT_FRIENDS_QUERY, id);

    }

    @Override
    public void deleteFriend(Long firstUserId, Long secondUserId) {
        super.delete(DELETE_FRIEND_QUERY,firstUserId,secondUserId);
    }

    @Override
    public Collection<User> getCommonFriends(Long firstUserId, Long secondUserId) {
        return super.findMany(SELECT_COMMON_FRIENDS,firstUserId,secondUserId);
    }
}
