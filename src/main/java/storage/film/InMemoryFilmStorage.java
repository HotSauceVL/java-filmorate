package storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private Map<Long, Film> films = new HashMap<>();
    private Long currentId = 0L;

    private Long getNextId() {
        return ++currentId;
    }
    @Override
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getById(Long id) {
       checkId(id);
       return films.get(id);
    }


    @Override
    public Film add(Film film) {
       if (validation(film)) {
           Long id = getNextId();
           film.setId(id);
           films.put(id, film);
           return film;
       } else {
           throw new ValidationException("Дата релиза не может быть раньше 28.12.1895");
       }
    }

    @Override
    public Film update(Film film) {
        if (validation(film)) {
            checkId(film.getId());
            films.put(film.getId(), film);
            return film;
        } else {
            throw new ValidationException("Дата релиза не может быть раньше 28.12.1895");
        }
    }

    @Override
    public void delete(Long id) {
        checkId(id);
        films.remove(id);
    }

    private boolean validation(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean checkId(Long id){
        if (films.containsKey(id))
            return true;
        else
            throw new FilmNotFoundException("Фильма с таким id не существует");
    }
}
