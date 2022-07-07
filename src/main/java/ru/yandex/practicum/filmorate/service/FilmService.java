package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Slf4j
@ComponentScan({"storage"})
@Service
public class FilmService {
    private FilmStorage filmStorage;
    private UserStorage userStorage;
    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                       @Qualifier("userDbStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(Long filmId, Long userId) {
        filmStorage.checkId(filmId);
        userStorage.checkId(userId);
        Film film = filmStorage.getById(filmId);
        film.addLike(userId);
        filmStorage.update(film);
    }

    public void removeLike(Long filmId, Long userId) {
        filmStorage.checkId(filmId);
        userStorage.checkId(userId);
        Film film = filmStorage.getById(filmId);
        film.removeLike(userId);
        filmStorage.update(film);
    }

    public Collection<Film> getMostLikedFilms(int count) {
        return filmStorage.getMostLikedFilms(count);
    }
}
