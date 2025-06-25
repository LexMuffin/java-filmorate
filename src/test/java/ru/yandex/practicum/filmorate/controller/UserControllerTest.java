package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void getAllUsersShouldReturnEmptyAnd200Code() throws Exception {
        mvc.perform(get("/users"))
                .andExpect(status().isOk());
    }



    @Test
    public void postUserAndGetStatusIsOk() throws Exception {
        String jsonString = "{\n" +
                "\"login\": \"common\",\n" +
                "\"email\": \"friend@common.ru\",\n" +
                "\"birthday\": \"2000-08-20\"" +
                "}\n";

        mvc.perform(post("/users")
                        .contentType("application/json")
                        .content(jsonString)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("friend@common.ru"));
    }

    @Test
    public void postUserAndGetStatusBadRequest() throws Exception {
        String jsonString = "{\n" +
                            "\"login\": \"dolore ullamco\",\n" +
                            "\"email\": \"yandex@mail.ru\",\n" +
                            "\"birthday\": \"2446-08-20\"" +
                            "}\n";

        mvc.perform(post("/users")
                        .contentType("application/json")
                        .content(jsonString)
                ).andExpect(status().isBadRequest());
    }

}
