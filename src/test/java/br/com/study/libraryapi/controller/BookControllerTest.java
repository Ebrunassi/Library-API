package br.com.study.libraryapi.controller;

import br.com.study.libraryapi.dto.BookDTO;
import br.com.study.libraryapi.exception.BusinessException;
import br.com.study.libraryapi.model.entity.Book;
import br.com.study.libraryapi.service.BookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc
public class BookControllerTest {

    public static String BOOK_API = "/api/books";

    @Autowired
    MockMvc mvc;    // Mock the requisitions
    @MockBean
    BookService service;

    @Test
    @DisplayName("Must create a book successfully")
    public void createBookTest() throws Exception{

        BookDTO dto = BookDTO.builder()
                .author("Francis Chan")
                .title("Crazy Love")
                .isbn("001")
                .build();
        Book savedBook = Book.builder().author("Francis Chan")
                .id(10L)
                .title("Crazy Love")
                .isbn("001")
                .build();

        BDDMockito.given(service.save(Mockito.any(Book.class))).willReturn(savedBook);
        String json = new ObjectMapper().writeValueAsString(dto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("title").value(dto.getTitle()))
                .andExpect(jsonPath("author").value(dto.getAuthor()))
                .andExpect(jsonPath("isbn").value(dto.getIsbn()))
        ;
    }

    @Test
    @DisplayName("Must not create a book due to insufficient informations and must throw validation error")
    public void createInvalidBookTest() throws Exception {
        String json = new ObjectMapper().writeValueAsString(new BookDTO());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(BOOK_API)       // Empty object
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", Matchers.hasSize(3)));
    }

    @Test
    @DisplayName("Must throw an error due to trying to create a book with an isbn wich already exists")
    public void createBookWithDuplicatedIsbn() throws Exception {

        BookDTO dto = BookDTO.builder().author("Francis Chan").title("Crazy Love").isbn("001").build();
        BDDMockito.given(service.save(Mockito.any(Book.class))).willThrow(new BusinessException("Isbn already exists!"));

        String json = new ObjectMapper().writeValueAsString(dto);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(BOOK_API)       // Empty object
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", Matchers.hasSize( 1)))
                .andExpect(jsonPath("errors[0]").value("Isbn already exists!"));
    }

    @Test
    @DisplayName("Must return the book's informations")
    public void getBookDetailsTest() throws Exception {
        // Scenario (given)
        Long id = 1L;
        Book book = Book.builder().id(id).author("J.R.R. Tolkien").title("The Lord of the Rings").isbn("0001").build();
        BDDMockito.given(service.getById(id)).willReturn(Optional.of(book));

        // Execution (when)
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(BOOK_API.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON);
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("title").value(book.getTitle()))
                .andExpect(jsonPath("author").value(book.getAuthor()))
                .andExpect(jsonPath("isbn").value(book.getIsbn()));
    }

    @Test
    @DisplayName("Must fail due to search for nonexistent book")
    public void getNonexistentBook() throws Exception{
        // Scenario
        BDDMockito.given(service.getById(Mockito.anyLong()))
                .willReturn(Optional.empty());

        // Execution
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(BOOK_API.concat("/" + 1L))
                .accept(MediaType.APPLICATION_JSON);
        mvc.perform(request)
                .andExpect(status().isNotFound());
    }
}
