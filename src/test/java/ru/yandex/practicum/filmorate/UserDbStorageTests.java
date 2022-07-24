package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageTests { //тест из примера падает, буду разбираться все тесты постмана проходит

    private final UserDbStorage userStorage;

    @Test
    public void testFindUserById() {
        assertEquals("Кверт", userStorage.getById(1L).getName());

        final Exception exception = assertThrows(UserNotFoundException.class,
                () -> userStorage.getById(25L));
        assertEquals("Пользователя с таким id не существует", exception.getMessage());
        Optional<User> userOptional = Optional.ofNullable(userStorage.getById(1L));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void testAddUser() {
        userStorage.add(new User( 0L ,"proverka@gmail.com", "login1", "Name1",
                LocalDate.of(1994,3, 4), null));
        assertEquals("Name1", userStorage.getById(3L).getName());
    }

    @Test
    public void testGetAllUsers() {
        assertEquals(3, userStorage.getAll().size());
    }

    @Test
    public void testUpdateUser() {
        userStorage.update(new User( 3L ,"proverka@gmail.com", "login1", "UpdateName1",
                LocalDate.of(1994,3, 4), null));
        assertEquals("UpdateName1", userStorage.getById(3L).getName());
    }
    @Test
    public void testDeleteUser() {
        assertEquals(3, userStorage.getAll().size());
        userStorage.delete(3L);
        assertEquals(2, userStorage.getAll().size());
    }
    @Test
    public void testCheckUserId() {
        final Exception exception = assertThrows(UserNotFoundException.class,
                () -> userStorage.getById(200L));
        assertEquals("Пользователя с таким id не существует", exception.getMessage());
        final Exception exception1 = assertThrows(UserNotFoundException.class,
                () -> userStorage.getById(-1L));
        assertEquals("Id не может быть меньше 0", exception1.getMessage());
    }

}
