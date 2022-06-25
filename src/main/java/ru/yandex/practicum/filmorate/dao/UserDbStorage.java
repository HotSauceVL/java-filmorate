package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcInsertOperations;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import storage.user.UserStorage;

import javax.swing.tree.RowMapper;
import javax.validation.constraints.Email;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@Primary
//@Qualifier("userDbStorage")
public class UserDbStorage implements UserStorage {

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage (){//JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = new JdbcTemplate();
    }

    @Override
    public List<User> getAll() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, ((rs, rowNum) -> makeUser(rs)));
    }

    @Override
    public User getById(Long id) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sql, ((rs, rowNum) -> makeUser(rs)), id);
    }

    @Override
    public User add(User user) {
        String sql = "INSERT INTO users (EMAIL, LOGIN, NAME, BIRTHDAY) values (?,?,?,?)";
        jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
        long id =
                jdbcTemplate.queryForRowSet("SELECT user_id FROM users WHERE user_id = MAX(user_id)").
                getLong("user_id");

        return user;
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
    @Override
    public boolean checkId(Long id) {
        return false;
    }

    private User makeUser(ResultSet rs) throws SQLException {
        long id = rs.getLong("user_id");
        String email = rs.getString("email");
        String login = rs.getString("login");
        String name = rs.getString("name");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();
        return new User(id, email, login, name, birthday);
    }
}
