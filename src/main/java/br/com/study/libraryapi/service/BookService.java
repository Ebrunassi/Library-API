package br.com.study.libraryapi.service;

import br.com.study.libraryapi.model.entity.Book;

import java.util.Optional;

public interface BookService {
    Book save(Book any);

    Optional<Book> getById(Long id);
}
