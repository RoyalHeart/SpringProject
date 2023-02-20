package com.example.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    SqlResource sqlResource = new ClasspathSqlResource("/sql/searchBook.sql");
    ConnectionProviderImpl connProvider = new ConnectionProviderImpl();
    int pageSize = 10;
    List<Book> books;

    @PostConstruct
    public void init() {
        books = bookRepository.findAll();
    }

    public Page<Book> getPage(Pageable pageable) {
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
        Page<Book> bookPage = new PageImpl<Book>(list, new PageRequest(currentPage,
                pageSize), books.size());
        return bookPage;
    }

    private Object getResponse(URL url) {
        try {
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            return content;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Book> getTrendingBooks() {
        List<Book> trendingBooks = new ArrayList<Book>();
        try {
            System.out.println(">>> start getting trending books");
            Object response = getResponse(new URL("https://openlibrary.org/trending/now.json"));
            Object object = new JSONParser().parse(response.toString());
            JSONObject jsonObject = (JSONObject) object;
            JSONArray works = (JSONArray) jsonObject.get("works");
            System.out.println(">>> content: " + works.get(0));
            for (Object book : works) {
                JSONObject bookJson = (JSONObject) book;
                String title = (String) bookJson.get("title");
                JSONArray authors;
                String author = "Anonymous";
                // some book have no author
                try {
                    authors = (JSONArray) bookJson.get("author_name");
                    author = (String) authors.get(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println(">>> title: " + title);
                System.out.println(">>> author: " + author);
                Book newBook = new Book();
                newBook.setTitle(title);
                newBook.setAuthor(author);
                trendingBooks.add(newBook);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return trendingBooks;
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

    public void saveBook(Book book) {
        bookRepository.save(book);
    }
}
