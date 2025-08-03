package ru.yandex.practicum.filmorate.storage.friend;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendStorage {

    List<User> getFriends(Long userId);

    List<User> getCommonFriends(Long userId1, Long userId2);

    void addFriend(Long userId, Long friendId);

    void removeFriend(Long userId, Long friendId);

    boolean friendshipCheck(Long userId, Long friendId);
}
