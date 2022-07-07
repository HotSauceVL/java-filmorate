package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.filmgenre.FilmGenreStorage;
import ru.yandex.practicum.filmorate.storage.likes.LikesStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MPARateDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final LikesStorage likesStorage;
    private final FilmGenreStorage filmGenreStorage;
    private final MPARateDao mpaRate;

    @Override
    public List<Film> getAll() {
        String sql = "SELECT FILM_ID, TITLE, DESCRIPTION, RELEASEDATE, DURATION, RATE, MR.NAME AS MPA " +
                "FROM FILMS JOIN MPA_RATE MR on MR.MPA_RATE_ID = FILMS.MPA_RATE_ID ";
        return jdbcTemplate.query(sql, ((rs, rowNum) -> makeFilm(rs)));
    }

    @Override
    public Film getById(Long id) {
        checkId(id);
        String sql ="SELECT * FROM FILMS WHERE FILM_ID = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeFilm(rs), id);
    }

    @Override
    public Film add(Film film) {
        if (validation(film)) {
            String sql = "INSERT INTO FILMS (TITLE, DESCRIPTION, RELEASEDATE, DURATION, RATE, MPA_RATE_ID) " +
                    "VALUES (:title, :description, :releaseDate, :duration, :rate, :mpa_rate_id)";
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
            MapSqlParameterSource parameterSource = new MapSqlParameterSource();
            parameterSource.addValue("title", film.getName());
            parameterSource.addValue("description", film.getDescription());
            parameterSource.addValue("releaseDate", film.getReleaseDate());
            parameterSource.addValue("duration", film.getDuration());
            parameterSource.addValue("rate", film.getRate());
            parameterSource.addValue("mpa_rate_id", film.getMpa().getId());
            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
            namedParameterJdbcTemplate.update(sql, parameterSource, keyHolder);
            film.setId(keyHolder.getKey().longValue());
            if (film.getGenres() != null && film.getGenres().size() > 0) {
                filmGenreStorage.insertFilmGenre(film);
            }
            if (film.getLikes() != null && film.getLikes().size() > 0) {
                likesStorage.addLikes(film);
            }
            return film;
        } else {
            throw new ValidationException("Дата релиза не может быть раньше 28.12.1895");
        }
    }

    @Override
    public Film update(Film film) {
        if (validation(film)) {
        checkId(film.getId());
        String sql = "UPDATE FILMS SET TITLE = ?, DESCRIPTION = ?, RELEASEDATE = ?, DURATION = ?, RATE = ?," +
                " MPA_RATE_ID = ? WHERE FILM_ID = ?";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getRate(), film.getMpa().getId(), film.getId());
        if (film.getGenres() != null && film.getGenres().size() > 0) {
            filmGenreStorage.insertFilmGenre(film);
        } else {
            filmGenreStorage.removeFilmGenre(film.getId());
        }
        if (film.getLikes() != null && film.getLikes().size() > 0) {
            likesStorage.addLikes(film);
        } else {
            likesStorage.removeLikes(film.getId());
        }
        Film updateFilmFromBD = getById(film.getId());
        if (film.getGenres() != null && film.getGenres().size() == 0) {
            updateFilmFromBD.setGenres(film.getGenres());
        }
        return updateFilmFromBD;
        } else {
            throw new ValidationException("Дата релиза не может быть раньше 28.12.1895");
        }
    }

    @Override
    public void delete(Long id) {
        if (checkId(id)) {
            String sql = "DELETE FROM FILMS WHERE FILM_ID = ?";
            jdbcTemplate.update(sql, id);
            filmGenreStorage.removeFilmGenre(id);
            likesStorage.removeLikes(id);
        }
    }

    @Override
    public boolean checkId(Long id) {
        if (id > 0) {
            String sql = "SELECT EXISTS (SELECT film_id FROM films WHERE film_id = ?)";
            if (Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, id)))
                return true;
            else
                throw new FilmNotFoundException("Фильма с таким id не существует");
        } else
            throw new FilmNotFoundException("Id не может быть меньше 0");
    }

    @Override
    public Collection<Film> getMostLikedFilms(int count) {
        String sql = "SELECT FILM_ID, TITLE, DESCRIPTION, RELEASEDATE, DURATION, RATE, MR.NAME AS MPA " +
                "FROM FILMS JOIN MPA_RATE MR on MR.MPA_RATE_ID = FILMS.MPA_RATE_ID " +
                "WHERE RATE <> NULL OR RATE <> 0 ORDER BY RATE DESC LIMIT ?";
        return jdbcTemplate.query(sql, ((rs, rowNum) -> makeFilm(rs)), count);
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        long id = rs.getLong("FILM_ID");
        String name = rs.getString("TITLE");
        String description = rs.getString("DESCRIPTION");
        LocalDate releaseDate = rs.getDate("RELEASEDATE").toLocalDate();
        Duration duration = Duration.ofSeconds(rs.getLong("DURATION"));
        Integer rate = rs.getInt("RATE");
        Collection<Genre> genre = filmGenreStorage.getFilmGenre(id);
        MPA mpa = mpaRate.getFilmMpa(id);
        Collection<Long> likes = likesStorage.getLikes(id);
        return new Film(id, name, description, releaseDate, duration, rate, genre , mpa, likes);
    }

    private boolean validation(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            return false;
        } else {
            return true;
        }
    }
}
