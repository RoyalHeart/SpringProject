/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.controller;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.persistence.model.Book;
import com.example.persistence.model.UserDetail;
import com.example.persistence.model.Wrapper;
import com.example.persistence.repo.UserRepository;
import com.example.service.BookService;
import com.example.service.DocPdf;
import com.example.service.ExcelService;
import com.example.service.ExportToExcel;

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
    UserRepository userRepository;

    @PostConstruct
    public void init() {
        // bookService.initializeBooks();
        // initializeUsers();
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

    @GetMapping("/home")
    public String getHome(Model model) {
        model.addAttribute("appName", appName);
        return "home";
    }

    @RequestMapping(value = "/book", method = RequestMethod.GET)
    public String getBook(Model model, @RequestParam("page") Optional<Integer> page) {
        int currentPage = page.orElse(1);
        int pageSize = 10;
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
        Page<Book> bookPage = bookService.getPage(pageable);
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

    @RequestMapping(value = "/fetchTrending", method = RequestMethod.POST)
    public String fetchTrending(HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        bookService.saveTrendingBooks();
        return "redirect:" + referer;
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String deleteBook(@PathVariable(name = "id") long id, Model model, HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        bookService.delete(id);
        logger.info(">>> Delelte book:" + id);
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
            logger.severe(">>> Edit error:" + e.getMessage());
        }
        return "redirect:" + referer;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String saveBook(@ModelAttribute("book") Book book,
            Model model, HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        logger.log(Level.INFO, ">>> Save: " + book.getAuthor() + ":" + book.getTitle() + "-" + book.getPublished());
        try {
            if (book.getImported() == null) {
                book.setImported(new Date(new java.util.Date().getTime()));
            }
            bookService.save(book);
            return "redirect:" + referer;
        } catch (Exception e) {
            logger.log(Level.SEVERE, ">>> Save error: " + e.getMessage());
            model.addAttribute("error", e.getMessage());
            if (e.getMessage().contains("ConstraintViolation")) {
                model.addAttribute("error", "Can not import book with same author and title");
            }
            if (e.getMessage().contains("NotBlank")) {
                model.addAttribute("error", "Author and titles must not be blank");
            }
            return "error";
        }
    }

    @RequestMapping(value = "/export", method = RequestMethod.POST)
    public String exportBook(@ModelAttribute(name = "wrapper") Wrapper wrapper,
            Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String referer = request.getHeader("Referer");
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy_HH.mm.ss");
            Date date = new Date(new java.util.Date().getTime());
            String currentTime = formatter.format(date);
            String home = System.getProperty("user.home");
            String filename = "Books_" + currentTime + ".xlsx";
            String exportPath = home + "/Downloads/" + filename;
            ExportToExcel.writeExcel(wrapper.getBooks(), exportPath);
            redirectAttributes.addFlashAttribute("exportExcelSuccessfully", "Exported at: " + exportPath);
        } catch (Exception e) {
            logger.log(Level.SEVERE, ">>> Export error: " + e.getMessage());
        }
        return "redirect:" + referer;
    }

    @RequestMapping(value = "/exportDoc", method = RequestMethod.POST)
    public String exportDoc(@ModelAttribute(name = "wrapper") Wrapper wrapper,
            Model model, HttpServletRequest request, RedirectAttributes redirectAttributes, Authentication auth) {
        String referer = request.getHeader("Referer");
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy_HH.mm.ss");
            Date date = new Date(new java.util.Date().getTime());
            String currentTime = formatter.format(date);
            String home = System.getProperty("user.home");
            String filename = "Books_" + currentTime + ".docx";
            String exportPath = home + "/Downloads/" + filename;
            redirectAttributes.addFlashAttribute("exportDocSuccessfully", "Exported at: " + exportPath);
            UserDetail user = new UserDetail();
            logger.log(Level.INFO, ">>> Username:" + auth.getName());
            logger.log(Level.INFO, ">>> Role:" + auth.getAuthorities().iterator().next().getAuthority());
            user.setUsername(auth.getName());
            user.setUser_role(auth.getAuthorities().iterator().next().getAuthority());
            DocPdf.exportDoc(wrapper.getBooks(), user, exportPath);
        } catch (Exception e) {
            logger.log(Level.SEVERE, ">>> Export error: " + e.getMessage());
        }
        return "redirect:" + referer;
    }

    @RequestMapping(value = "/exportPdf", method = RequestMethod.POST)
    public String exportPdf(@ModelAttribute(name = "wrapper") Wrapper wrapper,
            Model model, HttpServletRequest request, RedirectAttributes redirectAttributes, Authentication auth) {
        String referer = request.getHeader("Referer");
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy_HH.mm.ss");
            Date date = new Date(new java.util.Date().getTime());
            String currentTime = formatter.format(date);
            String home = System.getProperty("user.home");
            String filenamePdf = "Books_" + currentTime + ".pdf";
            String exportPath = home + "/Downloads/" + filenamePdf;
            UserDetail user = new UserDetail();
            user.setUsername(auth.getName());
            user.setUser_role(auth.getAuthorities().iterator().next().getAuthority());
            DocPdf.exportPdf(wrapper.getBooks(), user, exportPath);
            redirectAttributes.addFlashAttribute("exportPdfSuccessfully", "Exported at: " + exportPath);
        } catch (Exception e) {
            logger.log(Level.SEVERE, ">>> Export error: " + e.getMessage());
        }
        return "redirect:" + referer;
    }

    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public String importBook(@RequestParam("file") MultipartFile file, HttpServletRequest request,
            RedirectAttributes redirectAttributes) throws IOException, MaxUploadSizeExceededException {
        String referer = request.getHeader("Referer");
        if (ExcelService.isXLSX(file)) {
            redirectAttributes.addFlashAttribute("isExcel", true);
            Workbook workBook = ExcelService.loadBook(file.getInputStream());
            try {
                bookService.importFromExcel(workBook);
                redirectAttributes.addFlashAttribute("importSuccessfully",
                        "Imported successfully");
            } catch (Exception e) {
                String errorOutputPath = System.getProperty("user.dir");
                String exportPath = errorOutputPath + "/ErrorExcel_" + new java.util.Date().getTime() + ".xlsx";
                ExcelService.createOutputFile(workBook, exportPath);
                logger.log(Level.SEVERE, ">>> Error importing books: " + e.getMessage());
                redirectAttributes.addFlashAttribute("importError",
                        "Import has error - Export wrong Excel at: " + exportPath);
            }
        } else {
            logger.log(Level.SEVERE, ">>> Not Excel");
            redirectAttributes.addFlashAttribute("isExcel", false);
        }
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
