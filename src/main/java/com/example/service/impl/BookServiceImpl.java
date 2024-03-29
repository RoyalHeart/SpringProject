package com.example.service.impl;

import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
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
import org.springframework.transaction.annotation.Transactional;

import com.example.persistence.mirageRepo.BookRepo;
import com.example.persistence.model.Book;
import com.example.service.API;
import com.example.service.BookService;
import com.example.service.export_import.ImportFromExcel;

@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class BookServiceImpl implements BookService {
    static Logger logger = Logger.getLogger(BookServiceImpl.class.getName());
    private long OPENLIBRARY_ID = 1;
    private long GUTENDEX_ID = 2;
    private long CROSSREF_ID = 3;
    List<Book> searchBooks;

    @Autowired
    BookRepo bookRepository;

    // @Autowired
    // BookRepo bookRepo;

    public void initializeBooks() {
        try {
            Book book = new Book();
            Date date = new Date(new java.util.Date().getTime());
            book.setTitle("Mindset");
            book.setAuthor("Carol Dweck");
            book.setImported(date);
            book.setPublished((short) 2002);
            this.insert(book);
            book = new Book();
            book.setTitle("Operating System");
            book.setAuthor("Christen Baun");
            book.setImported(date);
            book.setPublished((short) 2002);
            this.insert(book);
            book = new Book();
            book.setTitle("Computer Network");
            book.setAuthor("Christen Baun");
            book.setImported(date);
            book.setPublished((short) 2002);
            this.insert(book);
            book = new Book();
            book.setTitle("Around the World in 100 Days");
            book.setAuthor("Jules Verne");
            book.setImported(date);
            book.setPublished((short) 2002);
            this.insert(book);
        } catch (Exception e) {
            logger.log(Level.SEVERE, ">>> Error init:" + e.getMessage());
        }
    }

    int pageSize = 10;

    public Page<Book> getPage(Pageable pageable) {
        int pageSize = this.pageSize;
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Book> list = bookRepository.getPage(startItem, startItem + pageSize);
        logger.info(list.toString());
        Page<Book> bookPage = new PageImpl<Book>(
                list,
                PageRequest.of(currentPage, pageSize, Sort.unsorted()),
                bookRepository.getBooksSize());
        return bookPage;
    }

    // @Scheduled(fixedRate = 30 * 60 * 1000 )
    public void saveOpenlibraryTrendingBooks() {
        CompletableFuture.runAsync(() -> {
            Iterator<Book> trendingBookItorator = getOpenlibraryTrendingBooks().iterator();
            while (trendingBookItorator.hasNext()) {
                try {
                    this.insert(trendingBookItorator.next());
                } catch (Exception e) {
                    logger.log(Level.SEVERE, ">>> Error saveTrendingBooks():" + e.getMessage());
                }
            }
        });
    }

    public void saveGutendexTrendingBooks() {
        CompletableFuture.runAsync(() -> {
            Iterator<Book> trendingBookItorator = getGutenbergTrendingBooks().iterator();
            while (trendingBookItorator.hasNext()) {
                try {
                    this.insert(trendingBookItorator.next());
                } catch (Exception e) {
                    logger.severe(">>> Error saveGutendexTrendingBooks():" + e.getMessage());
                }
            }
        });
    }

    public void saveCrossrefTrendingBooks() {
        CompletableFuture.runAsync(() -> {
            Iterator<Book> trendingBookItorator = getCrossrefTrendingBooks().iterator();
            while (trendingBookItorator.hasNext()) {
                try {
                    this.insert(trendingBookItorator.next());
                } catch (Exception e) {
                    logger.severe(">>> Error saveCrossrefTrendingBooks():" + e.getMessage());
                }
            }
        });
    }

    // @Scheduled(fixedRate = 1 * 60 * 1000)
    protected void testAsync() {
        logger.info(bookRepository.findByTitle("Demo").get(0).getTitle());
        // logger.info("Task1");
        // CompletableFuture.supplyAsync(this::getOpenlibraryTrendingBooks).thenAccept((List<Book>
        // books) -> {
        // books.forEach(System.out::println);
        // });
        // Thread thread = new Thread(() -> {
        // getOpenlibraryTrendingBooks();
        // getGutenbergTrendingBooks();
        // getCrossrefTrendingBooks();
        // });
        // thread.start();
        // CompletableFuture.runAsync(() -> {
        // try {
        // Thread.sleep(1000);
        // } catch (InterruptedException e) {
        // e.printStackTrace();
        // }
        // logger.info("Task2");
        // }).thenRun(() -> {
        // try {
        // Thread.sleep(1000);
        // } catch (InterruptedException e) {
        // e.printStackTrace();
        // }
        // logger.info("Task3");
        // });
        // logger.info("Task4");
    }

    private List<Book> getCrossrefTrendingBooks() {
        List<Book> trendingBooks = new ArrayList<Book>();
        try {
            logger.info(">>> start getting Crossref trending books");
            Object response = API
                    .fetch(new URL("https://api.crossref.org/works?sample=50&select=title,author,published"));
            Object object = new JSONParser().parse(response.toString());
            JSONObject jsonObject = (JSONObject) object;
            JSONObject message = (JSONObject) jsonObject.get("message");
            JSONArray items = (JSONArray) message.get("items");
            logger.info(">>> content: " + items.get(0));
            ;
            for (Object book : items) {
                JSONObject bookJson = (JSONObject) book;
                JSONArray titleJsonArray = (JSONArray) bookJson.get("title");
                String title = "";
                // some Crossref has no title
                try {
                    title = (String) titleJsonArray.get(0);
                } catch (Exception e) {
                    logger.severe(e.getMessage());
                }
                JSONArray authors;
                String author = "Anonymous";
                Short published = null;
                try {
                    JSONObject publishedJsonObject = (JSONObject) bookJson.get("published");
                    JSONArray dateJsonArray = (JSONArray) publishedJsonObject.get("date-parts");
                    JSONArray yearJsonArray = (JSONArray) dateJsonArray.get(0);
                    published = (short) ((Long) yearJsonArray.get(0)).intValue();
                } catch (Exception e) {
                    logger.severe(">>> Error getting publish year:" + e.getMessage());
                }
                // some book have no author
                try {
                    authors = (JSONArray) bookJson.get("author");
                    JSONObject authorJsonObject = (JSONObject) authors.get(0);
                    author = (String) authorJsonObject.get("given");
                    author += " ";
                    author += (String) authorJsonObject.get("family");
                } catch (Exception e) {
                    logger.log(Level.SEVERE, ">>> Error getting author:" + e.getMessage());
                }
                Book newBook = new Book();
                newBook.setTitle(title);
                newBook.setAuthor(author);
                newBook.setImported(new Date(new java.util.Date().getTime()));
                newBook.setPublished(published);
                newBook.setLibraryId(CROSSREF_ID);
                trendingBooks.add(newBook);
                logger.info(">>> Get: " + newBook);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return trendingBooks;
    }

    private List<Book> getGutenbergTrendingBooks() {
        List<Book> trendingBooks = new ArrayList<Book>();
        try {
            logger.info(">>> start getting Gutenberg trending books");
            int randomPage = new Random().nextInt() % 2191 + 1;
            randomPage = (randomPage > 0) ? randomPage : -randomPage;
            Object response = API.fetch(new URL("https://gutendex.com/books/?page=" + randomPage));
            Object object = new JSONParser().parse(response.toString());
            JSONObject jsonObject = (JSONObject) object;
            JSONArray results = (JSONArray) jsonObject.get("results");
            logger.info(">>> content: " + results.get(0));
            for (Object book : results) {
                JSONObject bookJson = (JSONObject) book;
                String title = (String) bookJson.get("title");
                JSONArray authors;
                String author = "Anonymous";
                Short published = null;
                // some book have no author
                try {
                    authors = (JSONArray) bookJson.get("authors");
                    JSONObject authorJsonObject = (JSONObject) authors.get(0);
                    author = (String) authorJsonObject.get("name");
                } catch (Exception e) {
                    logger.log(Level.SEVERE, ">>> Error getting author:" + e.getMessage());
                }
                Book newBook = new Book();
                newBook.setTitle(title);
                newBook.setAuthor(author);
                newBook.setImported(new Date(new java.util.Date().getTime()));
                newBook.setPublished(published);
                newBook.setLibraryId(GUTENDEX_ID);
                trendingBooks.add(newBook);
                logger.log(Level.INFO, ">>> Get: " + newBook);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return trendingBooks;
    }

    private List<Book> getOpenlibraryTrendingBooks() {
        List<Book> trendingBooks = new ArrayList<Book>();
        try {
            logger.info(">>> start getting Openlibrary trending books");
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
                newBook.setLibraryId(OPENLIBRARY_ID);
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
                this.insert(book);
                logger.info(
                        ">>> Imported books: " + book.getAuthor() + ":" + book.getTitle() + "-" + book.getPublished());
            } catch (Exception e) {
                logger.severe(">>> Error duplicate book:" + e.getMessage());
            }
        }
    }

    public Page<Book> getSearchPage(Pageable pageable) {
        int pageSize = this.pageSize;
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Book> list;
        if (searchBooks.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, searchBooks.size());
            list = searchBooks.subList(startItem, toIndex);
        }
        Page<Book> bookPage = new PageImpl<Book>(list, PageRequest.of(currentPage,
                pageSize, Sort.unsorted()), searchBooks.size());
        return bookPage;
    }

    @Override
    public List<Book> getSearchBooks() {
        return this.searchBooks;
    }

    public Page<Book> searchBook(Book book, short from, short to, Pageable pageable) {
        int pageSize = this.pageSize;
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        logger.info(">>> search book:" + book);
        try {
            List<Book> result = bookRepository.searchBook(book, from, to);
            searchBooks = result;
            List<Book> list;
            if (result.size() < startItem) {
                list = Collections.emptyList();
            } else {
                int toIndex = Math.min(startItem + pageSize, result.size());
                list = result.subList(startItem, toIndex);
            }
            Page<Book> bookPage = new PageImpl<Book>(list,
                    PageRequest.of(currentPage, pageSize, Sort.unsorted()),
                    result.size());
            return bookPage;
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
        return null;
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public void updateBook(Book book) {
        bookRepository.updateBook(book);
    }

    public void insert(Book book) {
        bookRepository.insert(book);
    }

    public void delete(long id) {
        bookRepository.deleteById(id);
    }

    public Optional<Book> findOne(long id) {
        return bookRepository.findById(id);
    }

}
