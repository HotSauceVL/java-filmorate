package ru.yandex.practicum.filmorate.storage.friends;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FriendsDbStorage implements FriendsStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Long> getFriends(long userId) {
        String sql = "SELECT FRIEND_ID FROM FRIENDS WHERE USER_ID = ?";
        return jdbcTemplate.query(sql, ((rs, rowNum) -> rs.getLong("FRIEND_ID")), userId);
    }

    @Override
    public void addFriend(User user) {
        removeFriend(user.getId());
        for (Long friendId : user.getFriends()) {
            String addFriendSql = "INSERT INTO FRIENDS (USER_ID, FRIEND_ID) VALUES (?, ?)";
            jdbcTemplate.update(addFriendSql, user.getId(), friendId);
        }
    }

    @Override
    public void removeFriend(long userId) {
        String deleteFriendSql = "DELETE FROM FRIENDS WHERE USER_ID = ?";
        jdbcTemplate.update(deleteFriendSql, userId);
    }
}
