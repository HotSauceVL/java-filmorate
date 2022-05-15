package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
public class FilmController {
    private Map<Long, Film> films = new HashMap<>();

    @PostMapping ("/films")
    public Film addFilm(@RequestBody Film film, HttpServletRequest request) {
        try {
            if (validation(film)) {
                log.info("Получен запрос к эндпоинту: {} {}, тело запроса {}", request.getMethod(),
                        request.getRequestURI(), film);
                films.put(film.getId(), film);
            }
        } catch (ValidationException e) {
            log.info("Ошибка валидации: "+ e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return film;
    }

    @PutMapping("/films")
    public String updateFilm(@RequestBody Film film, HttpServletRequest request) {
        String response = "Что-то пошло не так";
        try {
            if (validation(film)) {
                log.info("Получен запрос к эндпоинту: {} {}, тело запроса {}", request.getMethod(),
                        request.getRequestURI(), film);
                if (films.containsKey(film.getId())) {
                    films.put(film.getId(), film);
                    response = "Фильм успешно обновлен: " + film;
                } else {
                    films.put(film.getId(), film);
                    response = "Фильма с таким Id нет в фильмотеке, ваш фильм успешно добавлен: " + film;
                }
            }
        } catch (ValidationException e) {
            log.info("Ошибка валидации: "+ e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return response;
    }

    @GetMapping("/films")
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    private boolean validation(Film film) throws ValidationException {
        if (film.getName().isBlank()) {
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (film.getDescription().length() > 200 || film.getDescription().isBlank()) {
            throw new ValidationException("Описание фильма не может быть больше 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза не может быть раньше 28.12.1895");
        }
        if (film.getDuration().isNegative()) {
            throw new ValidationException("Продолжительность не может быть отрицательной");
        }
        return true;
    }
}
