package ru.javawebinar.topjava.repository.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.UsersUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class InMemoryUserRepositoryImpl implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepositoryImpl.class);
    private Map<Integer, User> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        UsersUtil.USERS.forEach(this::save);
    }

    public static void main(String[] args) {
        System.out.println(new InMemoryUserRepositoryImpl().get(2));
    }

    @Override
    public User save(User user) {

        if (user.isNew()) {
            user.setId(counter.incrementAndGet());
        }
         return repository.put(user.getId(), user);
    }

    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);
        if (repository.containsKey(id))
        {
            repository.remove(id);
            return true;
        }
        return false;
    }

    @Override
    public User get(int id) {
        log.info("get {}", id);
        if (repository.containsKey(id))
        {
            return repository.get(id);
        }
        return null;
    }

    @Override
    public List<User> getAll() {
        log.info("getAll");
        return UsersUtil.orderedUsersList(UsersUtil.USERS);
    }

    @Override
    public User getByEmail(String email) {
        log.info("getByEmail {}", email);

           return UsersUtil.orderedUsersList(UsersUtil.USERS).stream().
                   filter(user -> user.getEmail().equals(email))
                   .findFirst().orElse(null);
    }
}
