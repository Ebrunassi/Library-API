package br.com.study.libraryapi.controller;

import br.com.study.libraryapi.dto.BookDTO;
import br.com.study.libraryapi.model.entity.Book;
import br.com.study.libraryapi.service.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private BookService bookService;
    private ModelMapper modelMapper;

    public BookController(BookService service, ModelMapper mapper){
        this.bookService = service;
        this.modelMapper = mapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO createBook(@RequestBody BookDTO dto){

        Book bookEntity = modelMapper.map(dto, Book.class);
        bookEntity = bookService.save(bookEntity);

        return modelMapper.map(bookEntity, BookDTO.class);
    }
}
