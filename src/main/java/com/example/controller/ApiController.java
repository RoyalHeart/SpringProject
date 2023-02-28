package com.example.controller;

import java.sql.Date;
import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.persistence.model.Book;
import com.example.service.BookService;

@RestController
public class ApiController {
    private static Logger logger = Logger.getLogger(ApiController.class.getName());
    @Autowired
    BookService bookService;

    @RequestMapping(value = "/api", method = RequestMethod.GET)
    public String hello() {
        return "Hello, this is the Rest API service of the books management system\n" +
                "Here are the rules to connect and get access\n" +
                "Login with correct username and password form\n" +
                "Get at /api/books\n" +
                "Post at /api/books\n";
    }

    @RequestMapping(value = "/api/books", method = RequestMethod.GET)
    public List<Book> getAllBooks() {
        return bookService.findAll();
    }

    @RequestMapping(value = "/api/books", method = RequestMethod.POST)
    public Book postBook(@RequestBody Book book) {
        logger.info(">>> postBook():" + book);
        if (book.getImported() == null) {
            book.setImported(new Date(new java.util.Date().getTime()));
        }
        bookService.save(book);
        return book;
    }

    @RequestMapping(value = "/api/books/{id}", method = RequestMethod.GET)
    public Book getBook(@PathVariable(required = true) long id) {
        return bookService.findOne(id).orElse(null);
    }

    @RequestMapping(value = "/api/books/{id}", method = RequestMethod.DELETE)
    public String deleteBook(@PathVariable(required = true) long id) {
        bookService.delete(id);
        return "Delete book " + id + " successfully";
    }
}
