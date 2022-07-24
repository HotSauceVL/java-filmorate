package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDao;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreDao genreDao;

    public Collection<Genre> getAllGenre() {
        return genreDao.getAllGenre();
    }

    public Genre getGenreById(int id) {
        return genreDao.getGenreById(id);
    }
}
