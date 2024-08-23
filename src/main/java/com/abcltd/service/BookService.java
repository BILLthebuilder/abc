package com.abcltd.service;

import com.abcltd.dto.BookDto;
import com.abcltd.dto.GenericResponse;
import com.abcltd.model.Book;
import com.abcltd.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {


    private final BookRepository bookRepository;

    private final GenericService genericService;

    public ResponseEntity<GenericResponse> create(BookDto request, Errors errors) {
        return genericService.create(request, errors, Book.class, bookRepository);
    }

    public ResponseEntity<Optional<Book>> getOne(String id) {
        return genericService.getOne(id, this.bookRepository);
    }

    public List<Book> getAll(int page, int size, String sortBy, String sortOrder) {
        //return genericService.getAll(bookRepository,page, size,sortBy,sortOrder);
        return  bookRepository.findAll();
    }

    public ResponseEntity<GenericResponse> update(String id, BookDto request, Errors errors) {
        return genericService.update(UUID.fromString(id), request, errors, Book.class, bookRepository);
    }

    public ResponseEntity<GenericResponse> delete(String id) {
        return genericService.delete(id, bookRepository);
    }

}
