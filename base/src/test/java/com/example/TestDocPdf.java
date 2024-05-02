package com.example;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.example.core.user.entity.UserDetail;
import com.example.persistence.model.Book;
import com.example.service.export_import.DocPdf;

public class TestDocPdf {

    @Test
    public void testDocPdf() {
        try {
            List<Book> books = new ArrayList<Book>();
            Book book = new Book();
            book.setAuthor("Tome");
            book.setTitle("Hello world");
            book.setPublished((int) 2002);
            books.add(book);
            for (int i = 0; i < 400; i++) {
                book = new Book();
                book.setAuthor("Lily");
                book.setTitle("World");
                book.setPublished((int) 2002);
                books.add(book);
            }
            UserDetail userDetail = new UserDetail();
            userDetail.setUsername("admin");
            userDetail.setRole("ADMIN");
            String pathDoc = "project_out.docx";
            String pathPdf = "project_out.pdf";
            assertDoesNotThrow(() -> DocPdf.exportDoc(books, userDetail, pathDoc));
            assertDoesNotThrow(() -> DocPdf.exportPdf(books, userDetail, pathPdf));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
