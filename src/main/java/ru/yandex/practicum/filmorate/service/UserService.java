package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import storage.user.UserStorage;

import java.util.*;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(Long userId, Long friendId) {
        userStorage.checkId(userId);
        userStorage.checkId(friendId);
        userStorage.getById(userId).addFriend(friendId);
        userStorage.getById(friendId).addFriend(userId);
    }

    public void removeFriend(Long userId, Long friendId) {
        userStorage.checkId(userId);
        userStorage.checkId(friendId);
        userStorage.getById(userId).removeFriend(friendId);
        userStorage.getById(friendId).removeFriend(userId);
    }

    public List<User> getMutualFriends(Long userId, Long friendId) {
        Set<Long> mutualFriendsId = new HashSet<>(userStorage.getById(userId).getFriends());
        mutualFriendsId.retainAll(userStorage.getById(friendId).getFriends());
        List<User> mutualFriends = new ArrayList<>();
        for (Long id : mutualFriendsId) {
            mutualFriends.add(userStorage.getById(id));
        }
        return mutualFriends;
    }


}
