package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public class MPADaoImpl implements MPARateDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<MPA> getAllMPA() {
        String sql = "SELECT * FROM MPA_RATE";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new MPA(rs.getInt("MPA_RATE_ID"),
                rs.getString("NAME")));
    }

    @Override
    public MPA getMPAById(int id) {
        checkId(id);
        String sql = "SELECT * FROM MPA_RATE WHERE MPA_RATE_ID = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new MPA(rs.getInt("MPA_RATE_ID"),
                rs.getString("NAME")), id);
    }

    @Override
    public MPA getFilmMpa(long filmId) {
        String sql = "SELECT MPA_RATE_ID, NAME FROM MPA_RATE WHERE MPA_RATE_ID" +
                " IN (SELECT MPA_RATE_ID FROM FILMS WHERE FILM_ID = ?)";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                new MPA(rs.getInt("MPA_RATE_ID"), rs.getString("NAME")), filmId);
    }

    private boolean checkId(int id) {
        if (id > 0) {
            String sql = "SELECT EXISTS (SELECT MPA_RATE_ID FROM MPA_RATE WHERE MPA_RATE_ID = ?)";
            if (Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, id)))
                return true;
            else
                throw new NotFoundException("Рейтинга с таким id не существует");
        } else
            throw new NotFoundException("Id не может быть меньше 1");
    }
}
