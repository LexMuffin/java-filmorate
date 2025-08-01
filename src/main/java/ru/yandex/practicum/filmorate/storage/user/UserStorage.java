package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {

    User createUser(User newUser);

    User updateUser(User newUser);

    Optional<User> getUserById(Long userId);

    Collection<User> getAllUsers();

    boolean existsUserById(Long userId);
}
