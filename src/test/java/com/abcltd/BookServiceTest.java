package com.abcltd;


import com.abcltd.dto.BookDto;
import com.abcltd.dto.GenericResponse;
import com.abcltd.dto.Status;
import com.abcltd.model.Book;
import com.abcltd.repository.BookRepository;
import com.abcltd.service.BookService;
import com.abcltd.service.GenericService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private GenericService genericService;

    @InjectMocks
    private BookService bookService;

    private BookDto bookDto;
    private Book book;
    private Errors errors;

    @BeforeEach
    public void setUp() {
        bookDto = new BookDto("Tittle2", "Bill", 2029 );
        book = new Book();
        errors = mock(Errors.class);
    }

    @Test
    public void testCreateBook() {
        GenericResponse response = new GenericResponse( "Book created successfully", Status.SUCCESS);
        when(genericService.create(any(BookDto.class), any(Errors.class), eq(Book.class), any(BookRepository.class)))
                .thenReturn(ResponseEntity.ok(response));

        ResponseEntity<GenericResponse> result = bookService.create(bookDto, errors);

        assertEquals(Status.SUCCESS, result.getBody().status());
        assertEquals("Book created successfully", result.getBody().message());
        verify(genericService, times(1)).create(any(BookDto.class), any(Errors.class), eq(Book.class), any(BookRepository.class));
    }

    @Test
    public void testGetOneBook() {
        String bookId = String.valueOf(book.getId());
        ResponseEntity<Optional<Book>> optionalBook = ResponseEntity.of(Optional.of(Optional.of(book)));
        lenient().when(genericService.getOne(bookId, bookRepository))
                .thenReturn(optionalBook);

        ResponseEntity<Optional<Book>> result = bookService.getOne(bookId);

        assertTrue(result.getBody().isPresent());
        verify(genericService, times(1)).getOne(anyString(), any(BookRepository.class));
    }

    @Test
    public void testGetAllBooks() {
        when(bookRepository.findAll()).thenReturn(List.of(book));

        List<Book> result = bookService.getAll(0, 10, "id", "asc");

        assertEquals(1, result.size());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    public void testUpdateBook() {
        GenericResponse response = new GenericResponse("Book updated successfully",Status.SUCCESS);
        when(genericService.update(any(UUID.class), any(BookDto.class), any(Errors.class), eq(Book.class), any(BookRepository.class)))
                .thenReturn(ResponseEntity.ok(response));

        ResponseEntity<GenericResponse> result = bookService.update(String.valueOf(UUID.randomUUID()),bookDto,errors);

        assertEquals(Status.SUCCESS, result.getBody().status());
        assertEquals("Book updated successfully", result.getBody().message());
        verify(genericService, times(1)).update(any(UUID.class), any(BookDto.class), any(Errors.class), eq(Book.class), any(BookRepository.class));
    }

    @Test
    public void testDeleteBook() {
        GenericResponse response = new GenericResponse("Book deleted successfully",Status.SUCCESS);
        when(genericService.delete(anyString(), any(BookRepository.class)))
                .thenReturn(ResponseEntity.ok(response));

        ResponseEntity<GenericResponse> result = bookService.delete(String.valueOf(UUID.randomUUID()));

        assertEquals(Status.SUCCESS, result.getBody().status());
        assertEquals("Book deleted successfully", result.getBody().message());
        verify(genericService, times(1)).delete(anyString(), any(BookRepository.class));
    }
}
