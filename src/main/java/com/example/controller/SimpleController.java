/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.controller;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.persistence.mirageRepo.UserDetailRepository;
import com.example.persistence.model.Book;
import com.example.persistence.model.UserDetail;
import com.example.persistence.model.Wrapper;
import com.example.security.Validate;
import com.example.service.BookService;

@Controller
@ComponentScan("com.example.service")
public class SimpleController {
    static Logger logger = Logger.getLogger(SimpleController.class.getName());

    void initializeUsers() {
        try {
            UserDetail user = new UserDetail();
            user.setUsername("admin");
            user.setUser_role("ADMIN");
            user.setUser_password("$2a$12$Jt8ENKHcdh28mkizdJfEc.ekBTcRRRX9Cp3bz5Ze.dYnUHL3QbRmK");
            userRepository.save(user);
            user = new UserDetail();
            user.setUsername("user");
            user.setUser_role("USER");
            user.setUser_password("$2a$12$Jt8ENKHcdh28mkizdJfEc.ekBTcRRRX9Cp3bz5Ze.dYnUHL3QbRmK");
            user = new UserDetail();
            user.setUsername("u..");
            user.setUser_role("USER");
            user.setUser_password("$2a$12$Jt8ENKHcdh28mkizdJfEc.ekBTcRRRX9Cp3bz5Ze.dYnUHL3QbRmK");
            userRepository.save(user);
        } catch (Exception e) {
            logger.log(Level.SEVERE, ">>> Init error: " + e.getMessage());
        }
    }

    @Value("${spring.application.name}")
    String appName;

    @Autowired
    private BookService bookService;

    @Autowired
    private UserDetailRepository userRepository;

    @PostConstruct
    public void init() {
        bookService.initializeBooks();
        initializeUsers();
        // bookService.saveTrendingBooks();
    }

    @GetMapping("/")
    public String getBase(Model model, Authentication auth, RedirectAttributes redirectAttributes) {
        model.addAttribute("appName", appName);
        redirectAttributes.addFlashAttribute("username", auth.getName());
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

    @GetMapping("/signup")
    public String getSignup(
            @RequestParam(required = false) Optional<String> username, Model model) {
        model.addAttribute("usernameError", null);
        model.addAttribute("username", "");
        model.addAttribute("passwordError", null);
        model.addAttribute("error", "");
        model.addAttribute("newUser", new UserDetail());
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute("newUser") UserDetail newUser, Model model) {
        String username = newUser.getUsername();
        if (!Validate.isUsernameValid(username)) {
            model.addAttribute("usernameError", "Invalid username");
            return "/signup";
        }
        String plainPassword = "";
        try {
            logger.info(newUser.toString());
            plainPassword = newUser.getUser_password();
        } catch (Exception e) {
            logger.severe(">>> Password Error" + e.getMessage());
            model.addAttribute("passwordError", "Invalid password");
            return "/signup";
        }
        try {
            logger.info(">>> plain password:" + plainPassword);
            String hashPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
            logger.info(">>> hash password:" + hashPassword);
            newUser.setUser_password(hashPassword);
            newUser.setUser_role("USER");
        } catch (Exception e) {
            logger.severe(">>> Password Error:" + e.getMessage());
            return "/signup";
        }
        try {
            userRepository.save(newUser);
        } catch (Exception e) {
            logger.severe(">>> Username error:" + e.getMessage());
            model.addAttribute("usernameError", "Username already exist");
            return "/signup";
        }
        return "/login";
    }

    @GetMapping("/home")
    public String getHome(Model model, Authentication auth) throws InterruptedException {
        model.addAttribute("appName", appName);
        model.addAttribute("username", auth.getName());
        return "home";
    }

    @GetMapping("/book")
    public String getBook(Model model, @RequestParam("page") Optional<Integer> page) {
        int currentPage = page.orElse(1);
        int pageSize = 10;
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
        Page<Book> bookPage = bookService.getPage(pageable);
        Wrapper wrapper = new Wrapper();
        wrapper.setBooks(bookPage.getContent());
        int totalPages = bookPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        boolean haveBook = (bookPage.getContent().size() > 0) ? true : false;
        model.addAttribute("bookPage", bookPage);
        model.addAttribute("wrapper", wrapper);
        model.addAttribute("searchBook", new Book());
        model.addAttribute("haveBook", haveBook);
        return "book";
    }

    @PostMapping("/fetchOpenlibraryTrending")
    public String fetchOpenlibraryTrending(HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        bookService.saveOpenlibraryTrendingBooks();
        return "redirect:" + referer;
    }

    @PostMapping("/fetchGutendexTrending")
    public String fetchGutendexTrending(HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        bookService.saveGutendexTrendingBooks();
        return "redirect:" + referer;
    }

    @PostMapping("/fetchCrossrefTrending")
    public String fetchCrossrefTrending(HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        bookService.saveCrossrefTrendingBooks();
        return "redirect:" + referer;
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable(name = "id") long id, Model model, HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        bookService.delete(id);
        logger.info(">>> Delelte book:" + id);
        return "redirect:" + referer;
    }

    @GetMapping("/edit/{id}")
    public String editBook(@PathVariable(name = "id") long id, Model model, HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        try {
            Book editBook = bookService.findOne(id).get();
            model.addAttribute("editBook", editBook);
            return "edit";
        } catch (Exception e) {
            logger.severe(">>> Edit error:" + e.getMessage());
        }
        return "redirect:" + referer;
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("editBook") Book book,
            Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String referer = request.getHeader("Referer");
        logger.log(Level.INFO, ">>> Update: " + book.getAuthor() + ":" + book.getTitle() + "-" + book.getPublished());
        try {
            if (book.getImported() == null) {
                book.setImported(new Date(new java.util.Date().getTime()));
            }
            book.setLibraryId(7);
            bookService.updateBook(book);
            redirectAttributes.addFlashAttribute("success", "Update successfully");
            return "redirect:" + referer;
        } catch (Exception e) {
            logger.log(Level.SEVERE, ">>> Save error: " + e.getMessage());
            model.addAttribute("error", e.getMessage());
            if (e.getMessage().contains("UNIQUE")) {
                redirectAttributes.addFlashAttribute("error", "Can not import book with same author and title");
            }
            if (e.getMessage().contains("NotBlank")) {
                redirectAttributes.addFlashAttribute("error", "Author and titles must not be blank");
            }
            return "redirect:" + referer;
            // return "error";
        }
    }

    @PostMapping("/save")
    public String saveBook(@ModelAttribute("book") Book book,
            Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String referer = request.getHeader("Referer");
        logger.log(Level.INFO, ">>> Save: " + book.getAuthor() + ":" + book.getTitle() + "-" + book.getPublished());
        try {
            if (book.getImported() == null) {
                book.setImported(new Date(new java.util.Date().getTime()));
            }
            book.setLibraryId(7);
            bookService.insert(book);
            redirectAttributes.addFlashAttribute("success", "Add successfully");
            return "redirect:" + referer;
        } catch (Exception e) {
            logger.log(Level.SEVERE, ">>> Save error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            if (e.getMessage().contains("UNIQUE")) {
                redirectAttributes.addFlashAttribute("error", "Can not import book with same author and title");
            }
            if (e.getMessage().contains("NotBlank")) {
                redirectAttributes.addFlashAttribute("error", "Author and titles must not be blank");
            }
            return "redirect:" + referer;
        }
    }

    @GetMapping("/add")
    public String addBook(
            Model model) {
        model.addAttribute("book", new Book());
        return "add";
    }

    @GetMapping("/search")
    public String getSearchBook(Model model,
            @RequestParam("page") Optional<Integer> page) {
        int currentPage = page.orElse(1);
        int pageSize = 10;
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
        Page<Book> searchBookPage = bookService.getSearchPage(pageable);
        int totalPages = searchBookPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        Wrapper wrapper = new Wrapper();
        wrapper.setBooks(bookService.getSearchBooks());
        model.addAttribute("searchBookPage", searchBookPage);
        model.addAttribute("wrapper", wrapper);
        model.addAttribute("searchBook", new Book());
        return "book";
    }

    @PostMapping("/search")
    public String searchBook(@ModelAttribute("searchBook") Book searchBook, Model model,
            @RequestParam(required = false) String from, @RequestParam(required = false) String to,
            @RequestParam(name = "page", required = false) Optional<Integer> page,
            @RequestParam(name = "searchPage", required = false) Optional<Integer> searchPage) {
        short fromShort = 0;
        short toShort = 0;
        if (!from.isEmpty()) {
            fromShort = Short.parseShort(from);
            if (fromShort == 0)
                fromShort = 1; // change from 0 to 1 for sql to work
        }
        if (!to.isEmpty()) {
            toShort = Short.parseShort(to);
        }
        // if only have "to", and no "from", change from to 1
        if (toShort != 0) {
            if (fromShort == 0) {
                fromShort = 1;
            }
        }
        int currentPage = searchPage.orElse(1);
        int pageSize = 10;
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
        Page<Book> searchBookPage = bookService.searchBook(searchBook, fromShort, toShort, pageable);
        int totalPages = searchBookPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        Wrapper wrapper = new Wrapper();
        wrapper.setBooks(bookService.getSearchBooks());
        model.addAttribute("wrapper", wrapper);
        model.addAttribute("searchBook", new Book());
        model.addAttribute("searchBookPage", searchBookPage);
        return "book";
    }
}
