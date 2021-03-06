package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friends.FriendsStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@ComponentScan(basePackages={"com.sample"})
@Qualifier("userDbStorage")
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {


    private final JdbcTemplate jdbcTemplate;
    private final FriendsStorage friendsStorage;

    @Override
    public List<User> getAll() {
        String sql = "SELECT * FROM users";
        List<User> users = jdbcTemplate.query(sql, ((rs, rowNum) -> makeUser(rs)));
        users.stream().forEach(user -> user.getFriends().addAll(friendsStorage.getFriends(user.getId())));
        return users;
    }

    @Override
    public User getById(Long id) {
        checkId(id);
        String sql = "SELECT * FROM users WHERE user_id = ?";
        User user = jdbcTemplate.queryForObject(sql, ((rs, rowNum) -> makeUser(rs)), id);
        user.getFriends().addAll(friendsStorage.getFriends(user.getId()));
        return user;
    }

    @Override
    public User add(User user) {
        String sql = "INSERT INTO users (EMAIL, LOGIN, NAME, BIRTHDAY) values (:email,:login,:name,:birthday)";
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("email", user.getEmail());
        parameters.addValue("login", user.getLogin());
        parameters.addValue("name", user.getName());
        parameters.addValue("birthday", user.getBirthday());
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, parameters, generatedKeyHolder);
        user.setId(generatedKeyHolder.getKey().longValue());
        if (user.getFriends() != null && user.getFriends().size() > 0) {
            friendsStorage.addFriend(user);
        }
        return user;
    }

    @Override
    public User update(User user) {
        checkId(user.getId());
        String sql = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE user_id = ?";
        jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        if (user.getFriends() != null && user.getFriends().size() > 0) {
            friendsStorage.addFriend(user);
        } else {
            friendsStorage.removeFriend(user.getId());
        }
        return user;
    }

    @Override
    public void delete(Long userId) {
        if (checkId(userId)) {
            String sql = "DELETE FROM users WHERE user_id = ?";
            jdbcTemplate.update(sql, userId);
            friendsStorage.removeFriend(userId);
        }
    }
    @Override
    public boolean checkId(Long id) {
        if (id > 0) {
            String sql = "SELECT EXISTS (SELECT USER_ID FROM USERS WHERE USER_ID = ?)";
            if (Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, id)))
                return true;
            else
                throw new UserNotFoundException("???????????????????????? ?? ?????????? id ???? ????????????????????");
        }  else
            throw new UserNotFoundException("Id ???? ?????????? ???????? ???????????? 0");
    }

    private User makeUser(ResultSet rs) throws SQLException {
        long id = rs.getLong("user_id");
        String email = rs.getString("email");
        String login = rs.getString("login");
        String name = rs.getString("name");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();
        Set<Long> friends = new HashSet<>(friendsStorage.getFriends(id));
        return new User(id, email, login, name, birthday, friends);
    }
}
