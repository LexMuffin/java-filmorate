package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FilmController.class)
public class FilmControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void getAllFilmsShouldReturnEmptyAnd200Code() throws Exception {
        mvc.perform(get("/films"))
                .andExpect(status().isOk());
    }

    @Test
    public void postFilmAndGetStatusIsOk() throws Exception {
        String jsonString = "{\n" +
                "\"name\": \"nisi eiusmod\",\n" +
                "\"description\": \"adipisicing\",\n" +
                "\"releaseDate\": \"1967-03-25\",\n" +
                "\"duration\": 100\n" +
                "}\n";
        mvc.perform(post("/films")
                        .contentType("application/json")
                        .content(jsonString)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("nisi eiusmod"));
    }

    @Test
    public void postFilmAndGetStatusBadRequestMethodArgumentNotValidException() throws Exception {
        String jsonString = """
                    {
                    "name": "Name",
                    "description": "Description",
                    "releaseDate": "1890-03-25",
                    "duration": -200
                    }
                    """;

        mvc.perform(post("/films")
                .contentType("application/json")
                .content(jsonString)
        ).andExpect(status().isBadRequest())
        .andExpect(result -> assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException()));
    }

    @Test
    public void postFilmAndGetStatusBadRequest() throws Exception {

        String jsonString = """
                {
                    "name": "Film name",
                    "description": "Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль. Здесь они хотят разыскать господина Огюста Куглова, который задолжал им деньги, а именно 20 миллионов. о Куглов, который за время «своего отсутствия», стал кандидатом Коломбани.",
                    "releaseDate": "1900-03-25",
                    "duration": 200
                }
                """;
        mvc.perform(post("/films")
                .contentType("application/json")
                .content(jsonString)
                ).andExpect(status().is4xxClientError());
    }
}
