package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import storage.user.UserStorage;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@ComponentScan({"storage.user"})
public class UserController {
    UserService userService;
    UserStorage userStorage;
    @Autowired
    public UserController(UserService userService, UserStorage userStorage) {
        this.userService = userService;
        this.userStorage = userStorage;
    }

    @PostMapping("/users")
    public User add(@Valid @RequestBody User user, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: {} {}, тело запроса {}", request.getMethod(),
                        request.getRequestURI(), user);
        return userStorage.add(user);
    }

    @PutMapping("/users")
    public User update(@Valid @RequestBody User user, HttpServletRequest request) {
       // try {
            log.info("Получен запрос к эндпоинту: {} {}, тело запроса {}", request.getMethod(),
                    request.getRequestURI(), user);
            return userStorage.update(user);
       /* } catch (IllegalArgumentException e) {
            log.info("Ошибка входных данных: ", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }*/
    }


    @GetMapping("/users")
    public List<User> getAll() {
        return userStorage.getAll();
    }

    @GetMapping("/users/{id}")
    public User getById(@PathVariable Long id) {
        return userStorage.getById(id);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public String addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.addFriend(id, friendId);
        return "Друг успешно добавлен";
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public String removeFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.removeFriend(id, friendId);
        return "Друг удален";
    }

    @GetMapping("/users/{id}/friends")
    public List<User> getFriends(@PathVariable Long id) {
       List<Long> friendsId = new ArrayList<>(userStorage.getById(id).getFriends());
       List<User> friends = new ArrayList<>();
       for (Long friendId : friendsId) {
           friends.add(userStorage.getById(friendId));
       }
       return friends;
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable Long id, @PathVariable Long otherId) {
        return userService.getMutualFriends(id, otherId);
    }

}
