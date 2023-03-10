package com.example.persistence.mirageRepo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.query.Param;

import com.example.persistence.model.Book;
import com.example.service.database.DbRepository;

import jp.xet.springframework.data.mirage.repository.query.Modifying;

public interface BookRepo extends DbRepository<Book, Long> {
    List<Book> findAll();

    int getBooksSize();

    Optional<Book> findById(@Param("id") long id);

    List<Book> findByTitle(@Param("title") String title);

    List<Book> getPage(@Param("startItem") int startItem, @Param("endItem") int endItem);

    List<Book> searchBook(Book book, short from, short to);

    @Modifying
    void deleteById(@Param("id") long id);

    @Modifying
    void insert(Book book);

    @Modifying
    void updateBook(Book book);
}