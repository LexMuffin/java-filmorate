package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.base.BaseStorage;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDbStorage extends BaseStorage<User> implements UserStorage {

    private static final String FIND_ALL_QUERY = "select * from users";
    private static final String FIND_BY_ID_QUERY = "select * from users where user_id = ?";
    private static final String INSERT_QUERY = "insert into users(name, email, login, birthday) values (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "update users set name = ?, email = ?, login = ?, birthday = ? WHERE user_id = ?";

    public UserDbStorage(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public List<User> getAllUsers() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Optional<User> getUserById(Long userId) {
        return findOne(FIND_BY_ID_QUERY, userId);
    }

    @Override
    public User createUser(User newUser) {
        long id = insert(
                INSERT_QUERY,
                newUser.getName(),
                newUser.getEmail(),
                newUser.getLogin(),
                newUser.getBirthday()
        );
        newUser.setId(id);
        return newUser;
    }

    @Override
    public User updateUser(User newUser) {
        update(
                UPDATE_QUERY,
                newUser.getName(),
                newUser.getEmail(),
                newUser.getLogin(),
                newUser.getBirthday(),
                newUser.getId()
        );

        return newUser;
    }

    @Override
    public boolean existsUserById(Long userId) {
        return getUserById(userId).isPresent();
    }
}
