package com.abcltd.repository;

import com.abcltd.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {
    List<Book>findAll();
    @Query(value = "SELECT * FROM book LIMIT 1", nativeQuery = true)
    Book findSingleRecord();

    @Query(value = "SELECT * FROM book LIMIT 1", nativeQuery = true)
    String findSingleRecord2();
}
