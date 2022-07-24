package ru.yandex.practicum.filmorate.storage.mpa;

import ru.yandex.practicum.filmorate.model.MPA;

import java.util.Collection;

public interface MPARateDao {
    Collection<MPA> getAllMPA();
    MPA getMPAById(int id);
    MPA getFilmMpa(long filmId);
}
