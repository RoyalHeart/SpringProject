package com.example.service;

import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.persistence.model.Book;
import com.example.persistence.model.BookParam;
import com.example.persistence.repo.BookRepository;
import com.example.service.database.ClasspathSqlResourceImpl;
import com.example.service.database.ConnectionProviderImpl;
import com.example.service.import_export.ImportFromExcel;
import com.miragesql.miragesql.SqlManager;
import com.miragesql.miragesql.SqlManagerImpl;
import com.miragesql.miragesql.SqlResource;

@Service
public class BookService {
    static Logger logger = Logger.getLogger(BookService.class.getName());
    @Autowired
    BookRepository bookRepository;

    public void initializeBooks() {
        try {
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
        } catch (Exception e) {
            logger.log(Level.SEVERE, ">>> Error init:" + e.getMessage());
        }
    }

    SqlManager sqlManager = new SqlManagerImpl();
    SqlResource sqlResource = new ClasspathSqlResourceImpl("/static/sql/searchBook.sql");
    List<List<Book>> bookPages = new ArrayList<List<Book>>();

    @PostConstruct
    public void init() {
        sqlManager.setConnectionProvider(connProvider);
    }

    ConnectionProviderImpl connProvider = new ConnectionProviderImpl();
    int pageSize = 10;

    public Page<Book> getPage(Pageable pageable) {
        List<Book> books = bookRepository.findAll();
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

    // @Scheduled(fixedRate = 30 * 60 * 1000 )
    public void saveTrendingBooks() {
        CompletableFuture.runAsync(() -> {
            Iterator<Book> trendingBookItorator = getTrendingBooks().iterator();
            while (trendingBookItorator.hasNext()) {
                try {
                    this.save(trendingBookItorator.next());
                } catch (Exception e) {
                    logger.log(Level.SEVERE, ">>> Error saveTrendingBooks():" + e.getMessage());
                }
            }
        });
    }

    // @Scheduled(fixedRate = 10000)
    // public void logCurrentTime() throws InterruptedException {
    // logger.info(new java.util.Date().toString());
    // }

    // @Scheduled(fixedRate = 1 * 60 * 1000)
    public void testAsync() {
        logger.info("Task1");
        CompletableFuture.supplyAsync(this::getTrendingBooks).thenAccept((List<Book> books) -> {
            books.forEach(System.out::println);
        });
        Thread thread = new Thread(()->{
            getTrendingBooks();
        });
        thread.start();
        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            logger.info("Task2");
        }).thenRun(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            logger.info("Task3");
        });
        logger.info("Task4");
    }

    private List<Book> getTrendingBooks() {
        List<Book> trendingBooks = new ArrayList<Book>();
        try {
            logger.info(">>> start getting trending books");
            Object response = API.fetch(new URL("https://openlibrary.org/trending/now.json"));
            Object object = new JSONParser().parse(response.toString());
            JSONObject jsonObject = (JSONObject) object;
            JSONArray works = (JSONArray) jsonObject.get("works");
            logger.info(">>> content: " + works.get(0));
            for (Object book : works) {
                JSONObject bookJson = (JSONObject) book;
                String title = (String) bookJson.get("title");
                JSONArray authors;
                String author = "Anonymous";
                Short published = null;
                try {
                    published = (short) ((Long) bookJson.get("first_publish_year")).intValue();
                } catch (Exception e) {
                    logger.log(Level.SEVERE, ">>> Error getting publish year:" + e.getMessage());
                }
                // some book have no author
                try {
                    authors = (JSONArray) bookJson.get("author_name");
                    author = (String) authors.get(0);
                } catch (Exception e) {
                    logger.log(Level.SEVERE, ">>> Error getting author:" + e.getMessage());
                }
                Book newBook = new Book();
                newBook.setTitle(title);
                newBook.setAuthor(author);
                newBook.setImported(new Date(new java.util.Date().getTime()));
                newBook.setPublished(published);
                trendingBooks.add(newBook);
                logger.log(Level.INFO, ">>> Get: " + newBook);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return trendingBooks;
    }

    public void importFromExcel(Workbook workBook) throws Exception {
        List<Book> books;
        try {
            books = ImportFromExcel.excelToBooks(workBook);
        } catch (Exception e) {
            throw e;
        }
        for (Book book : books) {
            try {
                this.save(book);
                logger.log(Level.INFO,
                        ">>> Imported books: " + book.getAuthor() + ":" + book.getTitle() + "-"
                                + book.getPublished());
            } catch (Exception e) {
                logger.log(Level.SEVERE, ">>> Error duplicate book:" + e.getMessage());
            }
        }
    }

    public List<Book> searchBook(Book book, short from, short to) {
        logger.info(">>> search book:" + book);
        BookParam bookParam = new BookParam();
        bookParam.setAuthor(book.getAuthor());
        bookParam.setTitle(book.getTitle());
        bookParam.setPublished(book.getPublished());
        bookParam.setFrom(from);
        bookParam.setTo(to);
        try {
            List<Book> result = sqlManager.getResultList(
                    Book.class, sqlResource, bookParam);
            // logger.info(">>> search: " + result);
            return result;
        } catch (Exception e) {
            logger.log(Level.SEVERE, ">>> Error searchBook():" + e.getMessage());
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
