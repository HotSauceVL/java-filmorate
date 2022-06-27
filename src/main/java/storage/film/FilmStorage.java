package storage.film;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;


public interface FilmStorage {
    List<Film> getAll();
    Film getById(Long id);
    Film add(Film film);
    Film update(Film film);
    void delete(Long id);
    public boolean checkId(Long id);
}
