package ru.yandex.practicum.filmorate.storage.filmgenre;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

public interface FilmGenreStorage {
    public void insertFilmGenre(Film film);
    public void removeFilmGenre(long filmId);
    Collection<Genre> getFilmGenre(long filmId);
}
