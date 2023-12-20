/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.persistence.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.persistence.model.Book;

public interface BookRepository extends CrudRepository<Book, Long> {
    List<Book> findByTitle(@Param("title") String title);

    @Query("""
            SELECT * FROM ( SELECT *, ROW_NUMBER() OVER (ORDER BY id) as row FROM book WHERE DELETED is null) a
            WHERE a.row > :startItem AND a.row <= :endItem
            """)
    List<Book> getPage(@Param("startItem") int startItem, @Param("endItem") int endItem);

    @Query("SELECT * FROM BOOK")
    List<Book> searchBook(@Param("book") Book book, @Param("from") short from, @Param("to") short to);
}
