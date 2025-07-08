package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public Collection<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    @GetMapping("/{userId}/friends")
    public Collection<User> getFriends(@PathVariable Long userId) {
        return userService.getFriends(userId);
    }

    @GetMapping("/{userId1}/friends/common/{userId2}")
    public Collection<User> getCommonFriends(
            @PathVariable Long userId1,
            @PathVariable Long userId2
    ) {
        return userService.getCommonFriends(userId1, userId2);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void addFriend(
            @PathVariable Long userId,
            @PathVariable Long friendId
    ) {
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void removeFriend(
            @PathVariable Long userId,
            @PathVariable Long friendId
    ) {
        userService.removeFriend(userId, friendId);
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User newUser) {
        return userService.createUser(newUser);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User newUser) {
        return userService.updateUser(newUser);
    }

}
