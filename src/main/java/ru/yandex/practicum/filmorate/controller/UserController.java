package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/filmorate")
public class UserController {
    private Map<Long, User> users = new HashMap<>();

    @PostMapping("/users")
    public User addUser(@RequestBody User user, HttpServletRequest request) {
        try {
            if (validation(user)) {
                log.info("Получен запрос к эндпоинту: {} {}, тело запроса {}", request.getMethod(),
                        request.getRequestURI(), user);
                users.put(user.getId(), user);

            }
        } catch (ValidationException e) {
            log.info("Ошибка валидации: "+ e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return user;
    }

    @PutMapping("/users")
    public String updateUser(@RequestBody User user, HttpServletRequest request) {
        String response = "Что-то пошло не так";
        try {
            if (validation(user)) {
                log.info("Получен запрос к эндпоинту: {} {}, тело запроса {}", request.getMethod(),
                        request.getRequestURI(), user);
                if (users.containsKey(user.getId())) {
                    users.put(user.getId(), user);
                    return user.toString();
                } else {
                    users.put(user.getId(), user);
                    return "Нет пользователя с таким Id, новый пользователь успено добавлен: " + user;
                }
            }
        } catch (ValidationException e) {
            log.info("Ошибка валидации: "+ e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return response;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    private boolean validation(User user) throws ValidationException {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("Email не может быть пустым и должен содержать @");
        }
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может содержать пробелы и быть пустым");
        }
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        return true;
    }
}
