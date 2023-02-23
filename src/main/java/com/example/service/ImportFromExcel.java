package com.example.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.example.persistence.model.Book;

public class ImportFromExcel {
    static String SHEET = "Books";

    public static List<Book> excelToBooks(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);

            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();

            List<Book> books = new ArrayList<Book>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();

                Book book = new Book();

                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    switch (cellIdx) {
                        case 0:
                            book.setId((long) currentCell.getNumericCellValue());
                            break;

                        case 1:
                            book.setAuthor(currentCell.getStringCellValue());
                            break;

                        case 2:
                            book.setTitle(currentCell.getStringCellValue());
                            break;

                        // case 3:
                        // book.setPublished(currentCell.getBooleanCellValue());
                        // break;

                        default:
                            break;
                    }
                    cellIdx++;
                }

                books.add(book);
            }

            workbook.close();

            return books;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }
}
