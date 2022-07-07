package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
public class FilmDbStorageTests {

    @Autowired
    private FilmDbStorage filmStorage;

    @Test
    public void testFindFilmById() {

        assertEquals("Название1", filmStorage.getById(1L).getName());

        final Exception exception = assertThrows(FilmNotFoundException.class,
                () -> filmStorage.getById(25L));
        assertEquals("Фильма с таким id не существует", exception.getMessage());
        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.getById(1L));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1L)
                );
    }
    @Test
    public void testAddFilm() {
        filmStorage.add(new Film( 0L ,"Создание", "Описание1",
                LocalDate.of(1994, 4, 3), Duration.ofSeconds(150),
                5, null, new MPA(1, null), null));
        assertEquals("Создание", filmStorage.getById(3L).getName());

    }

    @Test
    public void testGetAllFilms() {
        assertEquals(3, filmStorage.getAll().size());
    }
   @Test
    public void testUpdateFilm() {
       filmStorage.update(new Film( 3L ,"Обновление", "Описание1",
               LocalDate.of(1994, 4, 3), Duration.ofSeconds(150),
               5, null, new MPA(1, null), null));
       assertEquals("Обновление", filmStorage.getById(3L).getName());
    }
    @Test
    public void testDeleteFilm() {
        assertEquals(3, filmStorage.getAll().size());
        filmStorage.delete(3L);
        assertEquals(2, filmStorage.getAll().size());
    }
    @Test
    public void testCheckFilmId() {
        final Exception exception = assertThrows(FilmNotFoundException.class,
                () -> filmStorage.getById(200L));
        assertEquals("Фильма с таким id не существует", exception.getMessage());
        final Exception exception1 = assertThrows(FilmNotFoundException.class,
                () -> filmStorage.getById(-1L));
        assertEquals("Id не может быть меньше 0", exception1.getMessage());
    }
    @Test
    public void testGetMostLikedFilms() {
        List<Film> films = new ArrayList<>(filmStorage.getMostLikedFilms(10));
        assertEquals("Название2", films.get(0).getName());
    }
}
