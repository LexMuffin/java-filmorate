package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friend.FriendStorage;

import java.util.List;

@Service
@AllArgsConstructor
public class FriendService {

    private final FriendStorage friendStorage;

    public List<User> getFriends(Long userId) {
        return friendStorage.getFriends(userId);
    }

    public List<User> getCommonFriends(Long userId1, Long userId2) {
        return friendStorage.getCommonFriends(userId1, userId2);
    }

    public void addFriend(Long userId, Long friendId) {
        friendStorage.addFriend(userId, friendId);
    }

    public void removeFriend(Long userId, Long friendId) {
        friendStorage.removeFriend(userId, friendId);
    }
}
