package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;
    private final FriendService friendService;

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUserById(Long userId) {
        return userStorage.getUserById(userId).orElseThrow(() -> new NotFoundException("Фильм не найдем"));
    }

    public User createUser(User newUser) {
        return userStorage.createUser(newUser);
    }

    public User updateUser(User newUser) {
        if (!userStorage.existsUserById(newUser.getId())) {
            throw new NotFoundException("Пользователь не найден");
        }
        return userStorage.updateUser(newUser);
    }

    public void addFriend(Long userId, Long friendId) {
        if (!userStorage.existsUserById(userId) || !userStorage.existsUserById(friendId)) {
            throw new NotFoundException("Пользователь не найден");
        }
        friendService.addFriend(userId, friendId);
    }

    public void removeFriend(Long userId, Long friendId) {
        if (!userStorage.existsUserById(userId) || !userStorage.existsUserById(friendId)) {
            throw new NotFoundException("Пользователь не найден");
        }
        friendService.removeFriend(userId, friendId);
    }

    public Collection<User> getCommonFriends(Long userId1, Long userId2) {
        if (!userStorage.existsUserById(userId1) || !userStorage.existsUserById(userId2)) {
            throw new NotFoundException("Пользователь не найден");
        }
        return friendService.getCommonFriends(userId1, userId2);
    }

    public Collection<User> getFriends(Long userId) {
        if (!userStorage.existsUserById(userId)) {
            throw new NotFoundException("Пользователь не найден");
        }
        return friendService.getFriends(userId);
    }

}
