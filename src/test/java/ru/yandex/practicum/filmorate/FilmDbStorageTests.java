package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.FilmDbStorage;

import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTests { //тест из примера падает, буду разбираться все тесты постмана проходит

    private final FilmDbStorage filmStorage;

    @Test
    public void testGetAllFilms() {

    }
    @Test
    public void testFindFilmById() {

        Optional<Film> userOptional = Optional.ofNullable(filmStorage.getById(1L));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1)
                );
    }
    @Test
    public void testAddFilm() {

    }
    @Test
    public void testUpdateFilm() {

    }
    @Test
    public void testDeleteFilm() {

    }
    @Test
    public void testCheckFilmId() {

    }
    @Test
    public void testGetMostLikedFilms() {

    }
}
