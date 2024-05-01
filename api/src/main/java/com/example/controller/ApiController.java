package com.example.controller;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.persistence.model.Book;
import com.example.service.book.IBookService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
public class ApiController {

    @Autowired
    IBookService bookService;

    @GetMapping("/")
    public String hello() {
        return "Hello, this is the Rest API service of the books management system\n" +
                "Here are the rules to connect and get access\n" +
                "Login with correct username and password form\n" +
                "Get at /api/books\n" +
                "Post at /api/books\n";
    }

    @GetMapping("/books")
    public Iterable<Book> getAllBooks() {
        return bookService.findAll();
    }

    @PostMapping("/books")
    public Book postBook(@RequestBody Book book) {
        log.info(">>> postBook():" + book);
        if (book.getImported() == null) {
            book.setImported(new Date(new java.util.Date().getTime()));
        }
        bookService.insert(book);
        return book;
    }

    @GetMapping("/books/{id}")
    public Book getBook(@PathVariable(required = true) long id) {
        return bookService.findOne(id).orElse(null);
    }

    @DeleteMapping("/books/{id}")
    public String deleteBook(@PathVariable(required = true) long id) {
        bookService.delete(id);
        return "Delete book " + id + " successfully";
    }
}
