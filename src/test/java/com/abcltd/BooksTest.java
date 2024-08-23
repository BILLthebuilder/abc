package com.abcltd;


import com.abcltd.dto.BookDto;
import com.abcltd.repository.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class BooksTest {
    @Autowired
    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();



    @Autowired
    BookRepository repository;

    @Test
    @Order(1)
    void bookShouldBeCreated() throws Exception {
        BookDto bookRequest = new BookDto("Tittle", "Bill", 2026 );

        mockMvc.perform(post("/api/v1/books").with(httpBasic("user","user@123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookRequest))
        ).andExpect(status().isCreated());
    }



    @Test
    @Order(2)
    void userShouldGetAll() throws Exception {
        mockMvc.perform(get("/api/v1/books").with(httpBasic("user","user@123"))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    @Order(4)
    void userShouldUpdate() throws Exception {
        var book = repository.findSingleRecord();
        BookDto bookRequest = new BookDto("Tittle2", "Bill", 2029 );

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/books/{id}", book.getId()).with(httpBasic("user","user@123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Order(5)
    void userShouldDelete() throws Exception {
        var book = repository.findSingleRecord();
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/books/{id}",book.getId()).with(httpBasic("user","user@123"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}