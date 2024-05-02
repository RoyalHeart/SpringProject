package com.example.service.book;

import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.persistence.model.Book;

public interface IBookService {
    public void initializeBooks();

    public Page<Book> getPage(Pageable pageable);

    public Page<Book> getSearchPage(Pageable pageable);

    public List<Book> getSearchBooks();

    public void saveOpenlibraryTrendingBooks();

    public void saveGutendexTrendingBooks();

    public void saveCrossrefTrendingBooks();

    public void importFromExcel(Workbook workBook) throws Exception;

    public Page<Book> searchBook(Book book, short from, short to, Pageable pageable);

    public void updateBook(Book book);

    public void insert(Book book);

    public Iterable<Book> findAll();

    public void delete(long id);

    public Optional<Book> findOne(long id);
}
