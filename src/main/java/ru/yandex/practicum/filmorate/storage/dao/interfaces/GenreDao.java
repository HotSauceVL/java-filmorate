package ru.yandex.practicum.filmorate.storage.dao.interfaces;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

public interface GenreDao {
    public Collection<Genre> getAllGenre();
    public Genre getGenreById(int id);
}
