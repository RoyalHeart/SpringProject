/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.persistence.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.example.persistence.model.Book;

// @Repository("postgresql")
public interface BookRepository extends CrudRepository<Book, Long> {
    List<Book> findByTitle(String title);

    Optional<Book> findById(long id);

    List<Book> findAll();
}
