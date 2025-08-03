package ru.yandex.practicum.filmorate.storage.friend;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.mappers.UserRowMapper;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FriendDbStorage implements FriendStorage {

    private static final String FIND_BY_ID_QUERY = "select\n" +
            "u.*\n" +
            "from friends f\n" +
            "left join users u on f.friend_id = u.user_id\n" +
            "where\n" +
            "f.user_id = ?";
    private static final String FIND_COMMON_BY_ID_QUERY = "select count(*) from friends where user_id = ? and friend_id = ?";
    private static final String GET_COMMON_FRIENDS = "select u.* \n" +
            "from users u, friends f, friends o\n" +
            "where \n" +
            "\tu.user_id = f.friend_id\n" +
            "\tand u.user_id = o.friend_id \n" +
            "\tand f.user_id = ? \n" +
            "\tand o.user_id = ?";
    private static final String INSERT_QUERY = "insert into friends(user_id, friend_id) values (?, ?)";
    private static final String DELETE_QUERY = "delete from friends where user_id = ? and friend_id = ?";
    private static final String UPDATE_FRIENDSHIP_QUERY = "update friends set friendship_state = true\n+" +
            "where user_id = ? and friend_id = ? or friend_id = ? and user_id = ?";
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> getFriends(Long userId) {
        return jdbcTemplate.query(FIND_BY_ID_QUERY, new UserRowMapper(), userId);
    }

    @Override
    public List<User> getCommonFriends(Long userId1, Long userId2) {
        return jdbcTemplate.query(GET_COMMON_FRIENDS, new UserRowMapper(), userId1, userId2);
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        jdbcTemplate.update(INSERT_QUERY, userId, friendId);
        if (friendshipCheck(userId, friendId)) {
            jdbcTemplate.update(UPDATE_FRIENDSHIP_QUERY, userId, friendId, userId, friendId);
        }
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        jdbcTemplate.update(DELETE_QUERY, userId, friendId);
    }

    @Override
    public boolean friendshipCheck(Long userId, Long friendId) {
        return jdbcTemplate.queryForObject(FIND_COMMON_BY_ID_QUERY, Integer.class, friendId, userId) > 0;
    }
}
