package com.example.persistence.model;

import java.util.ArrayList;
import java.util.List;

public class Wrapper {

    private List<Book> books = new ArrayList<Book>();

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public List<Book> getBooks() {
        return books;
    }
}
