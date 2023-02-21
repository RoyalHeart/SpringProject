/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.persistence.model.Book;
import com.example.persistence.model.UserDetail;
import com.example.persistence.repo.BookRepository;
import com.example.persistence.repo.UserRepository;
import com.example.service.BookService;

@Controller
@ComponentScan("com.example.service")
public class SimpleController {
    private void initializeBooks() {
        Book book = new Book();
        book.setTitle("Mindset");
        book.setAuthor("Carol Dweck");
        bookRepo.save(book);
        book = new Book();
        book.setTitle("Operating System");
        book.setAuthor("Christen Baun");
        bookRepo.save(book);
        book = new Book();
        book.setTitle("Computer Network");
        book.setAuthor("Christen Baun");
        bookRepo.save(book);
        book = new Book();
        book.setTitle("Around the World in 100 Days");
        book.setAuthor("Jules Verne");
        bookRepo.save(book);
    }

    @Value("${spring.application.name}")
    String appName;

    @Autowired
    private BookService bookService;

    @Autowired
    BookRepository bookRepo;

    @Autowired
    UserRepository userRepository;

    @PostConstruct
    public void init() {
        initializeBooks();
        bookRepo.save(bookService.getTrendingBooks());
        UserDetail user = new UserDetail();
        user.setUsername("admin");
        user.setUser_role("ADMIN");
        user.setUser_password("$2a$12$Jt8ENKHcdh28mkizdJfEc.ekBTcRRRX9Cp3bz5Ze.dYnUHL3QbRmK");
        userRepository.save(user);
    }

    @GetMapping("/")
    public String base(Model model) {
        model.addAttribute("appName", appName);
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String homePage(Model model) {
        model.addAttribute("appName", appName);
        return "home";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("appName", appName);
        return "login";
    }

    @RequestMapping(value = "/book", method = RequestMethod.GET)
    public String book(Model model, @RequestParam("page") Optional<Integer> page) {
        int currentPage = page.orElse(1);
        int pageSize = 10;
        Pageable pageable = new PageRequest(currentPage - 1, pageSize);
        Page<Book> bookPage = bookService.getPage(pageable);
        System.out.println(">>> bookPage:" + bookPage);
        model.addAttribute("bookPage", bookPage);
        int totalPages = bookPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("books", bookRepo.findAll());
        model.addAttribute("newBook", new Book());
        return "book";
    }

    @RequestMapping(value = "/del/{id}", method = RequestMethod.GET)
    public String deleteBook(@PathVariable(name = "id") long id, Model model) {
        bookRepo.delete(id);
        return "redirect:/book";
    }

    @RequestMapping(value = "/editBook/{id}", method = RequestMethod.GET)
    public String editBook(@PathVariable(name = "id") long id, Model model) {
        try {
            Book editBook = bookRepo.findOne(id).get();
            model.addAttribute("editBook", editBook);
            return "editBook";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/book";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String saveBook(@ModelAttribute("book") Book book,
            Model model) {
        try {
            bookRepo.save(book);
            return "redirect:/book";
        } catch (Exception e) {
            model.addAttribute("error", "All values must not be empty");
            return "errors";
        }
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addBook(
            Model model) {
        model.addAttribute("newBook", new Book());
        return "add";
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String searchBook(@ModelAttribute("book") Book searchBook, Model model) {
        List<Book> books = bookService.searchBook(searchBook);
        model.addAttribute("books", bookRepo.findAll());
        model.addAttribute("newBook", new Book());
        model.addAttribute("searchBooks", books);
        return "search";
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String searchBookGet(Model model) {
        model.addAttribute("newBook", new Book());
        return "search";
    }

    // @RequestMapping(value = "/delete", method = RequestMethod.POST)
    // public String deleteBook(@ModelAttribute("book") Book book) {
    // System.out.println(">>> delete");
    // bookRepo.delete(book.getId());
    // return "redirect:/book";
    // }
}
