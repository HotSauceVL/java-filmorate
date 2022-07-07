package ru.yandex.practicum.filmorate.storage.likes;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface LikesStorage {
    public List<Long> getLikes(long filmId);
    public void addLikes(Film film);
    public void removeLikes(long filmId);
}
