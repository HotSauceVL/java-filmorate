package ru.yandex.practicum.filmorate.storage.filmgenre;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

@Component
@Primary
@RequiredArgsConstructor
public class FilmGenreDbStorage implements FilmGenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void insertFilmGenre(Film film) {
        removeFilmGenre(film.getId());
        String addGenreSql = "INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES (?, ?)";
        film.getGenres().stream().forEach(genre -> jdbcTemplate.update(addGenreSql, film.getId(), genre.getId()));
    }

    @Override
    public void removeFilmGenre(long filmId) {
        String deleteGenresSql = "DELETE FROM FILM_GENRE WHERE FILM_ID = ?";
        jdbcTemplate.update(deleteGenresSql, filmId);
    }

    @Override
    public Collection<Genre> getFilmGenre(long filmId) {
        String sql = "SELECT GENRE.GENRE_ID, NAME FROM GENRE JOIN FILM_GENRE FG " +
                "ON GENRE.GENRE_ID = FG.GENRE_ID WHERE FG.FILM_ID = ?";
        Collection<Genre> genres = jdbcTemplate.query(sql, ((rs, rowNum) -> new Genre(rs.getInt("GENRE_ID"),
                rs.getString("NAME"))), filmId);
        if (genres.size() > 0)
            return genres;
        else
            return null;
    }
}
