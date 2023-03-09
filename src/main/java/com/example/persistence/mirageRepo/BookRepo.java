package com.example.persistence.mirageRepo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.query.Param;

import com.example.persistence.model.Book;
import com.example.service.database.DbRepository;

public interface BookRepo extends DbRepository<Book, Long> {
    List<Book> findAll();

    Optional<Book> findById(@Param("id") long id);

    void deleteById(@Param("id") long id);

    List<Book> findByTitle(@Param("title") String title);

    List<Book> findByComplexCondition(
            @Param("complex_param1") String cp1, @Param("complex_param2") int cp2);
}