package com.example.service.export_import;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.example.persistence.model.Book;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ImportFromExcel {
    static String SHEET = "Books";

    private static CellStyle createStyleForErrorInput(Sheet sheet) {
        // Create font
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
        font.setBold(true);
        font.setFontHeightInPoints((short) 14); // font size
        font.setColor(IndexedColors.WHITE.getIndex()); // text color

        // Create CellStyle
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        return cellStyle;
    }

    public static List<Book> excelToBooks(Workbook workbook) throws Exception {
        try {
            boolean haveParseError = false;
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

                    try {
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

                            case 3:
                                book.setPublished((int) currentCell.getNumericCellValue());
                                break;

                            default:
                                break;
                        }
                        cellIdx++;
                    } catch (Exception e) {
                        haveParseError = true;
                        log.error(">>> Error parsing cell" + e.getMessage());
                        currentCell.setCellStyle(createStyleForErrorInput(sheet));
                    }
                }
                book.setImported(new Date(new java.util.Date().getTime()));
                book.setLibraryId((long) 7);
                books.add(book);
            }
            if (haveParseError) {
                throw new Exception("Parse error");
            }
            ExcelService.createOutputFile(workbook,
                    "/ErrorExcel_" + new java.util.Date().getTime() + ".xlsx");
            workbook.close();
            return books;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }

}
