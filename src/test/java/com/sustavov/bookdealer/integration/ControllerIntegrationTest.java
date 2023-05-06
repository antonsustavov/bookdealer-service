package com.sustavov.bookdealer.integration;

import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;

import java.nio.charset.StandardCharsets;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Client API - Controller Integration Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ControllerIntegrationTest extends BaseIntegrationTest {

    @Test
    @SneakyThrows
    @Order(1)
    void shouldCreateAuthorSuccessfully() {
        var requestAuthorString = loadMessage("create-author-request");

        mvc.perform(post("/api/v1/authors")
                        .with(user("username").password("password").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestAuthorString))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Tolkien"))
                .andExpect(jsonPath("$.books.size()").value(2))
                .andDo(document("create-author-success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));

    }

    @Test
    @SneakyThrows
    @Order(2)
    void shouldGetAllSuccessfully() {
        mvc.perform(get("/api/v1/authors")
                        .with(user("username").password("password").roles("ADMIN"))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].books.size()").value(2))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andDo(document("getAll-author-success", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())));
    }

    @Test
    @SneakyThrows
    @Order(3)
    void shouldUpdateAuthorAndGetAllSuccessfully() {
        var requestAuthorString = loadMessage("update-author-request");

        mvc.perform(put("/api/v1/authors/{id}", 1L)
                        .with(user("username").password("password").roles("ADMIN"))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                        .content(requestAuthorString))
                .andExpect(status().isNoContent());

        mvc.perform(get("/api/v1/authors")
                        .with(user("username").password("password").roles("ADMIN"))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].books.size()").value(2))
                .andExpect(jsonPath("$[0].firstName").value("Alexandre"))
                .andDo(document("updated-author-success", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())));
    }

    @Test
    @SneakyThrows
    @Order(4)
    void shouldDeleteAuthor() {
        var requestAuthorString = loadMessage("update-author-request");

        mvc.perform(delete("/api/v1/authors/{id}", 1L)
                        .with(user("username").password("password").roles("ADMIN"))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                        .content(requestAuthorString))
                .andExpect(status().isNoContent());

        mvc.perform(get("/api/v1/authors")
                        .with(user("username").password("password").roles("ADMIN"))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty())
                .andDo(document("updated-author-success", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())));
    }

    @Test
    @SneakyThrows
    @Order(5)
    void shouldDeleteBookSuccessfully() {
        var requestAuthorString = loadMessage("create-author-request");

        mvc.perform(post("/api/v1/authors")
                        .with(user("username").password("password").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestAuthorString))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Tolkien"))
                .andExpect(jsonPath("$.books.size()").value(2))
                .andDo(document("create-author-success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));

        mvc.perform(delete("/api/v1/books/{id}", 3L)
                        .with(user("username").password("password").roles("ADMIN"))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        mvc.perform(get("/api/v1/books")
                        .with(user("username").password("password").roles("ADMIN"))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andDo(document("updated-author-success", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())));
    }

    @Test
    @SneakyThrows
    @Order(6)
    void shouldAuthorSortedSuccessfully() {
        var requestAuthorString = loadMessage("create-book-request");

        mvc.perform(post("/api/v1/authors")
                        .with(user("username").password("password").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestAuthorString))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Ranga"))
                .andExpect(jsonPath("$.lastName").value("Karanam"))
                .andExpect(jsonPath("$.books.size()").value(2))
                .andDo(document("create-author-success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));

        mvc.perform(get("/api/v1/authors/sorted/firstname")
                        .with(user("username").password("password").roles("ADMIN"))
                        .accept(MediaType.APPLICATION_JSON)
                        .param("firstName", "Ranga"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andDo(document("updated-author-success", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())));
    }

    @Test
    @SneakyThrows
    @Order(7)
    void shouldBookSortedSuccessfully() {
        var requestAuthorString = loadMessage("create-book-request");

        mvc.perform(get("/api/v1/books/sorted/isbn")
                        .with(user("username").password("password").roles("ADMIN"))
                        .accept(MediaType.APPLICATION_JSON)
                        .param("isbn", "888"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andDo(document("updated-author-success", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())));
    }


    @SneakyThrows
    private String loadMessage(String name) {
        ClassPathResource resource = new ClassPathResource(String.format("/messages/%s.json", name));
        return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
    }
}
