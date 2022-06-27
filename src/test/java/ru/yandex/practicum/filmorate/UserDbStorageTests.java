package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.UserDbStorage;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageTests { //тест из примера падает, буду разбираться все тесты постмана проходит

    private final UserDbStorage userStorage;

    @Test
    public void testGetAllUsers() {

    }

    @Test
    public void testFindUserById() {

        Optional<User> userOptional = Optional.ofNullable(userStorage.getById(1L));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void testAddUser() {

    }
    @Test
    public void testUpdateUser() {

    }
    @Test
    public void testDeleteUser() {

    }
    @Test
    public void testCheckUserId() {

    }

}
