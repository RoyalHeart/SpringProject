package com.example.controller;

import java.sql.Date;
import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.persistence.model.Book;
import com.example.service.BookService;

@RestController
public class ApiController {
    private static Logger logger = Logger.getLogger(ApiController.class.getName());
    @Autowired
    BookService bookService;

    @GetMapping("/api")
    public String hello() {
        return "Hello, this is the Rest API service of the books management system\n" +
                "Here are the rules to connect and get access\n" +
                "Login with correct username and password form\n" +
                "Get at /api/books\n" +
                "Post at /api/books\n";
    }

    @GetMapping("/api/books")
    public List<Book> getAllBooks() {
        return bookService.findAll();
    }

    @PostMapping("/api/books")
    public Book postBook(@RequestBody Book book) {
        logger.info(">>> postBook():" + book);
        if (book.getImported() == null) {
            book.setImported(new Date(new java.util.Date().getTime()));
        }
        bookService.save(book);
        return book;
    }

    @GetMapping("/api/books/{id}")
    public Book getBook(@PathVariable(required = true) long id) {
        return bookService.findOne(id).orElse(null);
    }

    @DeleteMapping("/api/books/{id}")
    public String deleteBook(@PathVariable(required = true) long id) {
        bookService.delete(id);
        return "Delete book " + id + " successfully";
    }
}
