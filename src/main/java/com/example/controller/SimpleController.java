/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.controller;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.multipart.MultipartFile;

import com.example.persistence.model.Book;
import com.example.persistence.model.UserDetail;
import com.example.persistence.model.Wrapper;
import com.example.persistence.repo.UserRepository;
import com.example.service.BookService;
import com.example.service.ExcelService;
import com.example.service.ExportToExcel;
import com.example.service.ImportFromExcel;

@Controller
@ComponentScan("com.example.service")
public class SimpleController {
    void initializeBooks() {
        Book book = new Book();
        Date date = new Date(new java.util.Date().getTime());
        book.setTitle("Mindset");
        book.setAuthor("Carol Dweck");
        book.setImported(date);
        book.setPublished((short) 2002);
        bookService.save(book);
        book = new Book();
        book.setTitle("Operating System");
        book.setAuthor("Christen Baun");
        book.setImported(date);
        book.setPublished((short) 2002);
        bookService.save(book);
        book = new Book();
        book.setTitle("Computer Network");
        book.setAuthor("Christen Baun");
        book.setImported(date);
        book.setPublished((short) 2002);
        bookService.save(book);
        book = new Book();
        book.setTitle("Around the World in 100 Days");
        book.setAuthor("Jules Verne");
        book.setImported(date);
        book.setPublished((short) 2002);
        bookService.save(book);
    }

    void initializeUsers() {
        UserDetail user = new UserDetail();
        user.setUsername("admin");
        user.setUser_role("ADMIN");
        user.setUser_password("$2a$12$Jt8ENKHcdh28mkizdJfEc.ekBTcRRRX9Cp3bz5Ze.dYnUHL3QbRmK");
        userRepository.save(user);
        user = new UserDetail();
        user.setUsername("user");
        user.setUser_role("USER");
        user.setUser_password("$2a$12$Jt8ENKHcdh28mkizdJfEc.ekBTcRRRX9Cp3bz5Ze.dYnUHL3QbRmK");
        userRepository.save(user);
    }

    @Value("${spring.application.name}")
    String appName;

    @Autowired
    private BookService bookService;

    @Autowired
    UserRepository userRepository;

    @PostConstruct
    public void init() {
        initializeBooks();
        initializeUsers();
        bookService.saveTrendingBooks();
    }

    @GetMapping("/")
    public String getBase(Model model) {
        model.addAttribute("appName", appName);
        return "redirect:/home";
    }

    @GetMapping("/login")
    public String getLogin(@RequestParam(required = false) Optional<String> logout,
            @RequestParam(required = false) Optional<String> authError,
            @RequestParam(required = false) Optional<String> username, Model model) {
        if (authError.isPresent()) {
            if (authError.get().contains("username")) {
                model.addAttribute("usernameError", "Username not exist");
                return "login";
            } else if (authError.get().contains("password")) {
                model.addAttribute("passwordError", "Wrong password");
                model.addAttribute("username", username.get());
                return "login";
            }
        }
        if (logout.isPresent()) {
            model.addAttribute("logout", "Logged out successfully!");
        }
        model.addAttribute("usernameError", null);
        model.addAttribute("username", "");
        model.addAttribute("passwordError", null);
        model.addAttribute("error", "");
        return "login";
    }

    @GetMapping("/home")
    public String getHome(Model model) {
        model.addAttribute("appName", appName);
        return "home";
    }

    @RequestMapping(value = "/book", method = RequestMethod.GET)
    public String getBook(Model model, @RequestParam("page") Optional<Integer> page) {
        int currentPage = page.orElse(1);
        int pageSize = 10;
        Pageable pageable = new PageRequest(currentPage - 1, pageSize);
        Page<Book> bookPage = bookService.getPage(pageable);
        System.out.println(">>> bookPage:" + bookPage);
        model.addAttribute("bookPage", bookPage);
        List<Book> exportBooks = new ArrayList<Book>();
        exportBooks.addAll(bookPage.getContent());
        Wrapper wrapper = new Wrapper();
        wrapper.setBooks(exportBooks);
        model.addAttribute("wrapper", wrapper);
        int totalPages = bookPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("books", bookService.findAll());
        return "book";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String deleteBook(@PathVariable(name = "id") long id, Model model, HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        bookService.delete(id);
        return "redirect:" + referer;
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String editBook(@PathVariable(name = "id") long id, Model model, HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        try {
            Book editBook = bookService.findOne(id).get();
            model.addAttribute("editBook", editBook);
            return "edit";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:" + referer;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String saveBook(@ModelAttribute("book") Book book,
            Model model, HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        try {
            bookService.save(book);
            return "redirect:" + referer;
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @RequestMapping(value = "/export", method = RequestMethod.POST)
    public String exportBook(@ModelAttribute(name = "wrapper") Wrapper wrapper,
            Model model, HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy_HH.mm.ss");
            Date date = new Date(new java.util.Date().getTime());
            String currentTime = formatter.format(date);
            String home = System.getProperty("user.home");
            String filename = "Books_" + currentTime + ".xlsx";
            ExportToExcel.writeExcel(wrapper.getBooks(), home + "/Downloads/" + filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:" + referer;
    }

    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public String importBook(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        System.out.println(">>> file" + file);
        if (ExcelService.isXLSX(file)) {
            System.out.println(">>> Excel");
            try {
                List<Book> books = ImportFromExcel.excelToBooks(file.getInputStream());
                for (Book book : books) {
                    System.out.println(">>> Imported books: " + book.getTitle());
                    try {
                        bookService.save(book);
                    } catch (Exception e) {
                        System.out.println(">>> Error duplicate: " + e.getMessage());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(">>> Error: " + e.getMessage());
            }
        } else {
            System.out.println(">>> Not Excel");
        }
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addBook(
            Model model) {
        model.addAttribute("newBook", new Book());
        return "add";
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String getSearchBook(Model model) {
        Wrapper wrapper = new Wrapper();
        model.addAttribute("wrapper", wrapper);
        model.addAttribute("newBook", new Book());
        return "search";
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String searchBook(@ModelAttribute("book") Book searchBook, Model model) {
        List<Book> books = bookService.searchBook(searchBook);
        Wrapper wrapper = new Wrapper();
        wrapper.setBooks(books);
        model.addAttribute("wrapper", wrapper);
        model.addAttribute("newBook", new Book());
        model.addAttribute("searchBooks", books);
        return "search";
    }
}
