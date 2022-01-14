package br.com.study.libraryapi.service;

import br.com.study.libraryapi.exception.BusinessException;
import br.com.study.libraryapi.model.entity.Book;
import br.com.study.libraryapi.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;

    public BookServiceImpl(BookRepository repository){
        this.bookRepository = repository;
    }

    @Override
    public Book save(Book book) {
        if(bookRepository.existsByIsbn(book.getIsbn()))
            throw new BusinessException("Isbn already exists!");
        return bookRepository.save(book);
    }

    @Override
    public Optional<Book> getById(Long id) {
        Optional<Book> returnedBook = bookRepository.findById(id);
        if(returnedBook.isPresent())
            return returnedBook;
        else
            throw new BusinessException("Book not found!");
    }
}
