package com.example.persistence.model;

import java.util.ArrayList;

public class Wrapper {

    private ArrayList<Book> books = new ArrayList<Book>();

    public void setBooks(ArrayList<Book> books) {
        this.books = books;
    }

    public ArrayList<Book> getBooks() {
        return books;
    }
}
