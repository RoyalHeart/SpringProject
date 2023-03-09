package com.example.service;

import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.persistence.model.Book;

public interface BookService {
    public void initializeBooks();

    public Page<Book> getPage(Pageable pageable);

    public void saveOpenlibraryTrendingBooks();

    public void saveGutendexTrendingBooks();

    public void saveCrossrefTrendingBooks();

    public void importFromExcel(Workbook workBook) throws Exception;

    public List<Book> searchBook(Book book, short from, short to);

    public void save(Book book);

    public List<Book> findAll();

    public void delete(Book book);

    public void delete(long id);

    public Optional<Book> findOne(long id);
}
