package com.example.service;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.ss.usermodel.Workbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.database.ConnectionProviderImpl;
import com.example.persistence.model.Book;
import com.example.persistence.repo.BookRepository;
import com.miragesql.miragesql.ClasspathSqlResource;
import com.miragesql.miragesql.SqlManager;
import com.miragesql.miragesql.SqlManagerImpl;
import com.miragesql.miragesql.SqlResource;

@Service
public class BookService {
    static Logger logger = Logger.getLogger(BookService.class.getName());
    @Autowired
    BookRepository bookRepository;

    public void initializeBooks() {
        Book book = new Book();
        Date date = new Date(new java.util.Date().getTime());
        book.setTitle("Mindset");
        book.setAuthor("Carol Dweck");
        book.setImported(date);
        book.setPublished((short) 2002);
        this.save(book);
        book = new Book();
        book.setTitle("Operating System");
        book.setAuthor("Christen Baun");
        book.setImported(date);
        book.setPublished((short) 2002);
        this.save(book);
        book = new Book();
        book.setTitle("Computer Network");
        book.setAuthor("Christen Baun");
        book.setImported(date);
        book.setPublished((short) 2002);
        this.save(book);
        book = new Book();
        book.setTitle("Around the World in 100 Days");
        book.setAuthor("Jules Verne");
        book.setImported(date);
        book.setPublished((short) 2002);
        this.save(book);
    }

    SqlManager sqlManager = new SqlManagerImpl();
    List<List<Book>> bookPages = new ArrayList<List<Book>>();
    SqlResource sqlResource = new ClasspathSqlResource("/static/sql/searchBook.sql");
    ConnectionProviderImpl connProvider = new ConnectionProviderImpl();
    int pageSize = 10;
    List<Book> books;

    public Page<Book> getPage(Pageable pageable) {
        books = bookRepository.findAll();
        int pageSize = this.pageSize;
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Book> list;
        if (books.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, books.size());
            list = books.subList(startItem, toIndex);
        }
        Page<Book> bookPage = new PageImpl<Book>(list, PageRequest.of(currentPage,
                pageSize, Sort.unsorted()), books.size());
        return bookPage;
    }

    public void saveTrendingBooks() {
        Iterator<Book> trendingBookItorator = getTrendingBooks().iterator();
        while (trendingBookItorator.hasNext()) {
            try {
                this.save(trendingBookItorator.next());
            } catch (Exception e) {
                // System.err.println(">>> Error saving:" + e.getMessage());
                logger.log(Level.WARNING, ">>> Error saving: " + e.getMessage());
            }
        }
    }

    private List<Book> getTrendingBooks() {
        List<Book> trendingBooks = new ArrayList<Book>();
        try {
            System.out.println(">>> start getting trending books");
            Object response = API.fetch(new URL("https://openlibrary.org/trending/now.json"));
            Object object = new JSONParser().parse(response.toString());
            JSONObject jsonObject = (JSONObject) object;
            JSONArray works = (JSONArray) jsonObject.get("works");
            System.out.println(">>> content: " + works.get(0));
            for (Object book : works) {
                JSONObject bookJson = (JSONObject) book;
                String title = (String) bookJson.get("title");
                JSONArray authors;
                String author = "Anonymous";
                Short published = null;
                try {
                    published = (short) ((Long) bookJson.get("first_publish_year")).intValue();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                // some book have no author
                try {
                    authors = (JSONArray) bookJson.get("author_name");
                    author = (String) authors.get(0);
                } catch (Exception e) {
                    logger.log(Level.SEVERE, ">>> Error getting author" + e.getMessage());
                }
                Book newBook = new Book();
                newBook.setTitle(title);
                newBook.setAuthor(author);
                newBook.setImported(new Date(new java.util.Date().getTime()));
                newBook.setPublished(published);
                trendingBooks.add(newBook);
                logger.log(Level.INFO, ">>> Save: " + author + " : " + title + "-" + published);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return trendingBooks;
    }

    public void importFromExcel(Workbook workBook) throws IOException {
        try {
            List<Book> books = ImportFromExcel.excelToBooks(workBook);
            for (Book book : books) {
                try {
                    this.save(book);
                    logger.log(Level.INFO,
                            ">>> Imported books: " + book.getAuthor() + ":" + book.getTitle() + "-"
                                    + book.getPublished());
                } catch (Exception e) {
                    logger.log(Level.SEVERE, ">>> Error duplicate book: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, ">>> Error importing books: " + e.getMessage());
            ExcelService.createOutputFile(workBook,
                    "/ErrorExcel_" + new java.util.Date().getTime() + ".xlsx");
        }
    }

    public List<Book> searchBook(Book book) {
        sqlManager.setConnectionProvider(connProvider);
        try {
            List<Book> result = sqlManager.getResultList(
                    Book.class, sqlResource, book);
            System.out.println(">>> search: " + result);
            return result;
        } catch (Exception e) {
            logger.log(Level.WARNING, ">>> Error search: " + e.getMessage());
            return null;
        } finally {
        }
    }

    public void save(Book book) {
        bookRepository.save(book);
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public void delete(Book book) {
        bookRepository.delete(book);
    }

    public void delete(long id) {
        bookRepository.deleteById(id);
    }

    public Optional<Book> findOne(long id) {
        return bookRepository.findById(id);
    }
}
