package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import storage.film.FilmStorage;
import storage.user.UserStorage;

import java.util.*;

@Slf4j
@Service
public class FilmService {
    FilmStorage filmStorage;
    UserStorage userStorage;
    private final Comparator<Film> comparator = ((o1, o2) -> {
        if (o1.getId() == o2.getId()) {
            return 0;
        } else {
            if (o2.getRate() > o1.getRate())
                return 1;
            else
                return -1;
        }
    });

    private final Set<Film> mostLikedFilms = new HashSet<>();

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(Long filmId, Long userId) {
        filmStorage.checkId(filmId);
        userStorage.checkId(userId);
        filmStorage.getById(filmId).addLike(userId);
        updateMostLikedFilms(filmId);
    }

    public void removeLike(Long filmId, Long userId) {
        filmStorage.checkId(filmId);
        userStorage.checkId(userId);
        filmStorage.getById(filmId).removeLike(userId);
        updateMostLikedFilms(filmId);
    }

    public Collection<Film> getMostLikedFilms() {
        Set<Film> sortedMostLikedFilms = new TreeSet<>(comparator); // с одним TreeSet не вышло,
        // компаратор не сравнивал айди всех эелементов и постоянно добавлялся повтор, гугление показало,
        // что такое бывает и самым нормальным ответом было использовать два множества
        sortedMostLikedFilms.addAll(mostLikedFilms);
        return sortedMostLikedFilms;
    }

    public void updateMostLikedFilms(Long filmId) {
        if (filmStorage.getById(filmId).getRate() > 0) {
            mostLikedFilms.remove(filmStorage.getById(filmId));
            mostLikedFilms.add(filmStorage.getById(filmId));
        } else
            mostLikedFilms.remove(filmStorage.getById(filmId));
        // оставил код неработающего метода, в котором добавлялись повторы после добавления лайка (а именно,
        // при добавлении лайка сравнивались id добявляемого элемента с элементом id 1,
        // который видимо стал вершиной дерева. В результате сравнение объектов с одинаковыми id так и не происходило.
        // Если получится узнать в чем ошибка, было бы супер. Компаратор остался таким же

        /*
        if (mostLikedFilms.contains(filmStorage.getById(filmId))) {
            log.info("Такого фильм есть в списке");
            log.info(String.valueOf(mostLikedFilms.contains(filmStorage.getById(filmId))));
            if (filmStorage.getById(filmId).getRate() > 0) {
                mostLikedFilms.remove(filmStorage.getById(filmId));
                log.info(String.valueOf(getMostLikedFilms()));
                mostLikedFilms.add(filmStorage.getById(filmId));
                log.info(String.valueOf(getMostLikedFilms()));
            } else {
                mostLikedFilms.remove(filmStorage.getById(filmId));
                log.info(String.valueOf(getMostLikedFilms()));
            }
        } else {
            log.info("Такого фильма нет в списке");
            if (filmStorage.getById(filmId).getRate() > 0)
                mostLikedFilms.add(filmStorage.getById(filmId));

            log.info(String.valueOf(getMostLikedFilms()));
        }*/
    }
}
