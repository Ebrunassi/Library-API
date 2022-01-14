package br.com.study.libraryapi.controller;

import br.com.study.libraryapi.dto.BookDTO;
import br.com.study.libraryapi.exception.ApiErrors;
import br.com.study.libraryapi.exception.BusinessException;
import br.com.study.libraryapi.model.entity.Book;
import br.com.study.libraryapi.service.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import javax.validation.*;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private BookService bookService;
    private ModelMapper modelMapper;

    public BookController(BookService service, ModelMapper mapper){
        this.bookService = service;
        this.modelMapper = mapper;
    }

    @GetMapping("/{id}")
    public BookDTO getBook(@PathVariable Long id){
        return bookService.getById(id)
                .map( book -> modelMapper.map(book, BookDTO.class))
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO createBook(@RequestBody @Valid BookDTO dto){

        Book bookEntity = modelMapper.map(dto, Book.class);
        bookEntity = bookService.save(bookEntity);

        return modelMapper.map(bookEntity, BookDTO.class);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleValidationExceptions(MethodArgumentNotValidException exception){
        BindingResult bindingResult = exception.getBindingResult();
        return new ApiErrors(bindingResult);
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleBusinessException(BusinessException exception){
        return new ApiErrors(exception);
    }
}
