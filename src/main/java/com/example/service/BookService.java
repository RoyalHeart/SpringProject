package com.example.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

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
    @Autowired
    BookRepository bookRepository;

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
                System.out.println(">>> Error:" + e.getMessage());
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
                Short published = 0;
                try {
                    published = (short) ((Long) bookJson.get("first_publish_year")).intValue();
                    System.out.println(">>> year: " + published);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                // some book have no author
                try {
                    authors = (JSONArray) bookJson.get("author_name");
                    author = (String) authors.get(0);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                System.out.println(">>> " + author + " : " + title + "-" + published);
                Book newBook = new Book();
                newBook.setTitle(title);
                newBook.setAuthor(author);
                newBook.setImported(new Date(new java.util.Date().getTime()));
                newBook.setPublished(published);
                trendingBooks.add(newBook);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return trendingBooks;
    }

    public void importFromExcel(InputStream inputStream) throws IOException {
        try {
            System.out.println(">>> Excel");
            List<Book> books = ImportFromExcel.excelToBooks(inputStream);
            for (Book book : books) {
                System.out.println(">>> Imported books: " + book.getTitle());
                try {
                    this.save(book);
                } catch (Exception e) {
                    System.out.println(">>> Error duplicate: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(">>> Error: " + e.getMessage());
        }
    }

    public List<Book> searchBook(Book book) {
        sqlManager.setConnectionProvider(connProvider);
        try {
            List<Book> result = sqlManager.getResultList(
                    Book.class, sqlResource, book);
            System.out.println(">>> search: " + result);
            return result;
        } catch (Exception ex) {
            System.out.println(ex);
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
