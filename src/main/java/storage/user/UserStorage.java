package storage.user;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
@Repository
public interface UserStorage {
    List<User> getAll();
    User getById(Long id);
    public User add(User user);
    public User update(User user);
    public void delete(Long id);
    public boolean checkId(Long id);
}
