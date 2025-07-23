package ru.yandex.practicum.filmorate.storage;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> usersEmailsSet = new HashSet<>();

    @PostConstruct
    public void initEmailSet() {
        users.values().forEach(user -> usersEmailsSet.add(user.getEmail()));
    }

    @Override
    public Collection<User> getAllUsers() {
        log.info("GET /users - получение списка всех пользователей");
        return users.values();
    }

    @Override
    public User getUserById(Long userId) {
        User user = users.get(userId);
        if (user == null) {
            log.error("Пользователь с идентификатором - \"{}\" не найден", userId);
            throw new NotFoundException("Пользователь не найден");
        }
        return user;
    }

    @Override
    public User createUser(User newUser) {
        log.info("POST /users - добавление нового пользователя");
        try {
            if (usersEmailsSet.contains(newUser.getEmail())) {
                log.error("Пользователь с почтой \"{}\" уже существует", newUser.getEmail());
                throw new DuplicatedDataException("Пользователь с такой почтой уже существует");
            }
            if (newUser.getName() == null || newUser.getName().isBlank()) {
                newUser.setName(newUser.getLogin());
                log.debug("Имя пользователя пустое, замена на \"{}\"", newUser.getLogin());
            }
            newUser.setId(getNextUserId());
            users.put(newUser.getId(), newUser);
            log.info("Пользователь с почтой \"{}\" создан", newUser.getEmail());
            usersEmailsSet.add(newUser.getEmail());
            return newUser;
        } catch (RuntimeException ex) {
            log.trace("Ошибка - \"{}\" при добавлении нового пользователя", ex.getMessage());
            throw ex;
        }
    }

    @Override
    public User updateUser(User newUser) {
        log.info("PUT /users - обновление существующего пользователя");
        try {
            User existingUser = users.get(newUser.getId());
            if (existingUser == null) {
                log.error("Пользователь с идентификатором - \"{}\" не найден", newUser.getId());
                throw new NotFoundException("Пользователь не найден");
            }
            if (usersEmailsSet.contains(newUser.getEmail())) {
                log.error("Пользователь с почтой \"{}\" уже существует", newUser.getEmail());
                throw new DuplicatedDataException("Пользователь с такой почтой уже существует");
            }
            if (newUser.getName() == null || newUser.getName().isBlank()) {
                newUser.setName(newUser.getLogin());
                log.debug("Имя пользователя пустое, замена на \"{}\"", newUser.getLogin());
            }
            existingUser.setName(newUser.getName());
            existingUser.setLogin(newUser.getLogin());
            existingUser.setEmail(newUser.getEmail());
            existingUser.setBirthday(newUser.getBirthday());
            log.info("Пользователь с почтой \"{}\" обновлен", newUser.getEmail());
            usersEmailsSet.add(newUser.getEmail());
            return existingUser;
        } catch (RuntimeException ex) {
            log.trace("Ошибка - \"{}\" при обновлении пользователя", ex.getMessage());
            throw ex;
        }
    }

    private Long getNextUserId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @Override
    public boolean existsUserById(Long userId) {
        return users.containsKey(userId);
    }
}
