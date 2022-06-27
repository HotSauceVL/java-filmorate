package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
public class User {
    private long id;
    @Email
    private String email;
    @NotBlank
    @Pattern(regexp = "^\\S*$")
    private String login;
    private String name;
    @Past
    private LocalDate birthday;
    private Set<Long> friends = new HashSet<>();

    public void addFriend(Long friendId) {
        friends.add(friendId);
    }

    public void addFriend(Collection<Long> friendList) {
        friends.addAll(friendList);
    }

    public void removeFriend(Long friendId) {
        friends.remove(friendId);
    }

    public User(long id, String email, String login, String name, LocalDate birthday, Set<Long> friends) {
        this.id = id;
        this.email = email;
        this.login = login;
        if (name.equals(""))
            this.name = login;
        else
            this.name = name;
        this.birthday = birthday;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
