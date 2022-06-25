package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import storage.film.FilmStorage;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Stream;

@Slf4j
@RestController
//@ComponentScan({"storage.film"})
@ComponentScan({"dao"})
@RequestMapping("/films")
@Qualifier("filmDbStorage")
public class FilmController {
    FilmService filmService;

    FilmStorage filmStorage;
    @Autowired
    public FilmController(FilmService filmService, FilmStorage filmStorage) {
        this.filmService = filmService;
        this.filmStorage = filmStorage;
    }

    @PostMapping
    public Film add(@Valid @RequestBody Film film, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: {} {}, тело запроса {}", request.getMethod(),
                    request.getRequestURI(), film);
        Film addedFilm = filmStorage.add(film);
        if (addedFilm.getRate() != 0) {
            filmService.updateMostLikedFilms(addedFilm.getId());
        }
        return addedFilm;
    }

    @PutMapping
    public @Valid Film update(@Valid @RequestBody Film film, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: {} {}, тело запроса {}", request.getMethod(),
                    request.getRequestURI(), film);
        Film updatedFilm = filmStorage.update(film);
        if (updatedFilm.getRate() != 0) {
            filmService.updateMostLikedFilms(updatedFilm.getId());
        }
        return updatedFilm;
    }

    @GetMapping
    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    @DeleteMapping("/{filmId}")
    public String delete(@PathVariable Long filmId) {
        filmStorage.delete(filmId);
        return "Фильм успешно удален";
    }


    @GetMapping("/{filmId}")
    public Optional<Film> getFilmById(@PathVariable Long filmId) {
        return Optional.ofNullable(filmStorage.getById(filmId));
    }

    @PutMapping("/{id}/like/{userId}")
    public String addLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.addLike(id, userId);
        return "Лайк успешно добавлен";
    }

    @DeleteMapping("/{id}/like/{userId}")
    public String removeLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.removeLike(id, userId);
        return "Лайк удален";
    }

    @GetMapping("/popular")
    public Stream<Film> getPopularFilms(@RequestParam(defaultValue = "10", required = false) Integer count) {
        if (count <= 0)
            throw new IncorrectParameterException("Параметр count не может быть меньше 1");
        return filmService.getMostLikedFilms().stream().limit(count);
    }

}
