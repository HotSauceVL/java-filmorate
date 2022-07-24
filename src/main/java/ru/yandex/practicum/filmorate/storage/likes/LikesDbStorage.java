package ru.yandex.practicum.filmorate.storage.likes;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LikesDbStorage implements LikesStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Long> getLikes(long filmId) {
        String sql = "SELECT USER_ID FROM LIKES WHERE FILM_ID = ?";
        return jdbcTemplate.query(sql, ((rs, rowNum) -> rs.getLong("USER_ID")), filmId);
    }

    @Override
    public void addLikes(Film film) {
        removeLikes(film.getId());
        String addFriendSql = "INSERT INTO LIKES (USER_ID, FILM_ID) VALUES (?, ?)";
        film.getLikes().stream().forEach(userId -> jdbcTemplate.update(addFriendSql, userId, film.getId()));
        /*for (Long userId : film.getLikes()) {
            String addFriendSql = "INSERT INTO LIKES (USER_ID, FILM_ID) VALUES (?, ?)";
            jdbcTemplate.update(addFriendSql, userId, film.getId());
        }*/
    }

    @Override
    public void removeLikes(long filmId) {
        String deleteLikesSql = "DELETE FROM LIKES WHERE FILM_ID = ?";
        jdbcTemplate.update(deleteLikesSql, filmId);
    }
}
