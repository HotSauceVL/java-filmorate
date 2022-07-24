package ru.yandex.practicum.filmorate.storage.friends;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendsStorage {
    List<Long> getFriends(long userId);
    void addFriend(User user);
    void removeFriend(long userId);
}
