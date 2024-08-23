package com.abcltd.controller;


import com.abcltd.dto.BookDto;
import com.abcltd.dto.GenericResponse;
import com.abcltd.model.Book;
import com.abcltd.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<GenericResponse> create(@RequestBody @Valid BookDto request, Errors errors) {
        return bookService.create(request, errors);
    }

    @GetMapping
    public List<Book> getAll(@RequestParam(name = "page", defaultValue = "0") int page,
                             @RequestParam(name = "size", defaultValue = "10") int size,
                             @RequestParam(name = "sort", required = false) String sortBy,
                             @RequestParam(name = "order", defaultValue = "asc") String sortOrder) {
        return bookService.getAll(page, size,sortBy,sortOrder);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Book>> getOne(@PathVariable String id) {
        return bookService.getOne(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GenericResponse> update(@RequestBody @Valid BookDto request, @PathVariable String id, Errors errors) {
        return bookService.update(id, request, errors);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse> delete(@PathVariable String id) {
        return bookService.delete(id);
    }
}

