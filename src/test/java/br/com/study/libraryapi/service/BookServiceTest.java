package br.com.study.libraryapi.service;

import br.com.study.libraryapi.exception.BusinessException;
import br.com.study.libraryapi.model.entity.Book;
import br.com.study.libraryapi.repository.BookRepository;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {

    BookService bookService;
    @MockBean
    BookRepository bookRepository;

    @BeforeEach
    public void setUp(){
        this.bookService = new BookServiceImpl(bookRepository);
    }

    @Test
    @DisplayName("Must save the book")
    public void saveBookTest(){
        // Scenario
        Book book = Book.builder()
                    .isbn("124789")
                    .author("Paul Washer")
                    .title("Walking with Jesus")
                .build();
        Book bookReturned = Book.builder()
                .id(1L)
                .isbn("124789")
                .author("Paul Washer")
                .title("Walking with Jesus")
                .build();

        Mockito.when(bookRepository.save(book)).thenReturn(bookReturned);
        Mockito.when(bookRepository.existsByIsbn(Mockito.anyString())).thenReturn(false);   // Even though it will return false by default, its a good practice to show it explicit

        // Execution
        Book savedBook = bookService.save(book);

        // Verification
        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getIsbn()).isEqualTo("124789");
        assertThat(savedBook.getTitle()).isEqualTo("Walking with Jesus");
        assertThat(savedBook.getAuthor()).isEqualTo("Paul Washer");
    }

    @Test
    @DisplayName("Must throw an error due to trying to create a book with an isbn wich already exists")
    public void shouldNotSaveBookWithDuplicatedIsbn(){
        // Scenario
        Book book = Book.builder().isbn("124789").author("Paul Washer").title("Walking with Jesus").build();
        Mockito.when(bookRepository.existsByIsbn(Mockito.anyString())).thenReturn(true);

        // Execution
        Throwable exception = Assertions.catchThrowable(() -> bookService.save(book));

        // Verification
        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Isbn already exists!");
        Mockito.verify(bookRepository, Mockito.never()).save(book);     // Verify if the method 'save' wasn't called


    }
}
