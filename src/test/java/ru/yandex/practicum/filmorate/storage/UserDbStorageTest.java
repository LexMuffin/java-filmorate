package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserDbStorageTest {
    private final UserStorage userStorage;

    @Test
    @Order(1)
    public void createUser() {
        User newUser = User.builder()
                .name("Test User")
                .email("testUser@email.com")
                .login("test_user")
                .birthday(LocalDate.of(1996, 4, 18))
                .build();

        Optional<User> userOptional = Optional.ofNullable(userStorage.createUser(newUser));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user -> {
                            assertThat(user).hasFieldOrPropertyWithValue("id", 1L);
                            assertThat(user).hasFieldOrPropertyWithValue("login", "test_user");
                            assertThat(user).hasFieldOrPropertyWithValue("name", "Test User");
                            assertThat(user).hasFieldOrPropertyWithValue("email", "testUser@email.com");
                            assertThat(user).hasFieldOrPropertyWithValue("birthday",
                                    LocalDate.of(1996, 4, 18));
                        }
                );
    }

    @Test
    @Order(2)
    public void updateUser() {
        User newUser = User.builder()
                .id(1L)
                .name("Test User1")
                .email("testUser1@email.com")
                .login("test_user1")
                .birthday(LocalDate.of(1996, 4, 18))
                .build();

        Optional<User> userOptional = Optional.ofNullable(userStorage.updateUser(newUser));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user -> {
                            assertThat(user).hasFieldOrPropertyWithValue("id", 1L);
                            assertThat(user).hasFieldOrPropertyWithValue("login", "test_user1");
                            assertThat(user).hasFieldOrPropertyWithValue("name", "Test User1");
                            assertThat(user).hasFieldOrPropertyWithValue("email", "testUser1@email.com");
                            assertThat(user).hasFieldOrPropertyWithValue("birthday",
                                    LocalDate.of(1996, 4, 18));
                        }
                );
    }

    @Test
    @Order(3)
    public void getUserById() {
        Optional<User> userOptional = userStorage.getUserById(1L);
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user -> {
                            assertThat(user).hasFieldOrPropertyWithValue("id", 1L);
                            assertThat(user).hasFieldOrPropertyWithValue("login", "test_user1");
                            assertThat(user).hasFieldOrPropertyWithValue("name", "Test User1");
                            assertThat(user).hasFieldOrPropertyWithValue("email", "testUser1@email.com");
                            assertThat(user).hasFieldOrPropertyWithValue("birthday",
                                    LocalDate.of(1996, 4, 18));
                        }
                );
    }

    @Test
    @Order(4)
    public void getAllUsers() {
        User newUser = User.builder()
                .name("Test User2")
                .email("testUser2@email.com")
                .login("test_user2")
                .birthday(LocalDate.of(1997, 5, 19))
                .build();
        userStorage.createUser(newUser);
        Collection<User> users = userStorage.getAllUsers();
        assertThat(users).asList().element(0).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(users).asList().element(1).hasFieldOrPropertyWithValue("id", 2L);
        assertThat(users).asList().element(1).hasFieldOrPropertyWithValue("email", "testUser2@email.com");
    }
}