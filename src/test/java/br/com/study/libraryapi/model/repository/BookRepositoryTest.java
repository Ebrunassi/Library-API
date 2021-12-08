package br.com.study.libraryapi.model.repository;

import br.com.study.libraryapi.model.entity.Book;
import br.com.study.libraryapi.repository.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest    // Will use the H2 memory database to execute the tests
public class BookRepositoryTest {

    @Autowired
    TestEntityManager entityManager;
    @Autowired
    BookRepository bookRepository;

    @Test
    @DisplayName("Must return true when a book with the same isbn already exists in repository")
    public void returnTrueWhenIsbnExists(){
        // Scenario
        String isbn = "123";
        entityManager.persist(Book.builder().title("The Lord of the Rings").author("J.R.R. Tolkien").isbn(isbn).build());

        // Execution
        boolean exists = bookRepository.existsByIsbn(isbn);

        // Verification
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Must return false when a book with the same isbn doesn't exists in repository")
    public void returnFalseWhenIsbnDoesntExists(){
        // Scenario
        String isbn = "123";

        // Execution
        boolean exists = bookRepository.existsByIsbn(isbn);

        // Verification
        assertThat(exists).isFalse();
    }

}
