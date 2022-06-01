package storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private Map<Long, User> users = new HashMap<>();
    private Long currentId = 0L;

    private Long getNextId() {
        return ++currentId;
    }
    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getById(Long id) {
        checkId(id);
        return users.get(id);
    }

    @Override
    public User add(User user) {
        Long id = getNextId();
        user.setId(id);
        users.put(id, user);
        return user;
    }

    @Override
    public User update(User user) {
        checkId(user.getId());
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    @Override
    public void delete(Long id) {
        if (checkId(id))
            users.remove(id);
    }

    @Override
    public boolean checkId(Long id){
        if (users.containsKey(id))
            return true;
        else
            throw new UserNotFoundException("Пользователя с таким id не существует");
    }
}
