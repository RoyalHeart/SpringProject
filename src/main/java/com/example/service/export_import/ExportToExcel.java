package com.example.service.export_import;

import java.io.IOException;
import java.sql.Date;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.example.persistence.model.Book;

@Service
public class ExportToExcel {
    public static final int COLUMN_INDEX_ID = 0;
    public static final int COLUMN_INDEX_AUTHOR = 1;
    public static final int COLUMN_INDEX_TITLE = 2;
    public static final int COLUMN_INDEX_PUBLISHED = 3;
    public static final int COLUMN_INDEX_IMPORTED = 4;
    private static CellStyle cellStyleFormatNumber = null;

    public static void writeExcel(Iterable<Book> books, String excelFilePath) throws IOException {
        // Create Workbook
        Workbook workbook = getWorkbook(excelFilePath);

        // Create sheet
        Sheet sheet = workbook.createSheet("Books"); // Create sheet with sheet name

        int rowIndex = 0;

        // Write header
        writeHeader(sheet, rowIndex);

        // Write data
        rowIndex++;
        for (Book book : books) {
            // Create row
            Row row = sheet.createRow(rowIndex);
            // Write data on row
            writeBook(book, row);
            rowIndex++;
        }

        // Write footer
        // writeFooter(sheet, rowIndex);

        // Auto resize column witdth
        int numberOfColumn = sheet.getRow(0).getPhysicalNumberOfCells();
        autosizeColumn(sheet, numberOfColumn);

        // Create file excel
        ExcelService.createOutputFile(workbook, excelFilePath);
    }

    // Create workbook
    private static Workbook getWorkbook(String excelFilePath) throws IOException {
        Workbook workbook = null;

        if (excelFilePath.endsWith("xlsx")) {
            workbook = new XSSFWorkbook();
        } else if (excelFilePath.endsWith("xls")) {
            workbook = new HSSFWorkbook();
        } else {
            throw new IllegalArgumentException("The specified file is not Excel file");
        }

        return workbook;
    }

    // Write header with format
    private static void writeHeader(Sheet sheet, int rowIndex) {
        // create CellStyle
        CellStyle cellStyle = createStyleForHeader(sheet);

        // Create row
        Row row = sheet.createRow(rowIndex);

        // Create cells
        Cell cell = row.createCell(COLUMN_INDEX_ID);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Id");

        cell = row.createCell(COLUMN_INDEX_AUTHOR);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Author");

        cell = row.createCell(COLUMN_INDEX_TITLE);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Title");

        cell = row.createCell(COLUMN_INDEX_PUBLISHED);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Published Year");

        cell = row.createCell(COLUMN_INDEX_IMPORTED);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Imported Date");
    }

    // Write data
    private static void writeBook(Book book, Row row) {
        if (cellStyleFormatNumber == null) {
            // Format number
            short format = (short) BuiltinFormats.getBuiltinFormat("#,##0");
            // DataFormat df = workbook.createDataFormat();
            // short format = df.getFormat("#,##0");

            // Create CellStyle
            Workbook workbook = row.getSheet().getWorkbook();
            cellStyleFormatNumber = workbook.createCellStyle();
            cellStyleFormatNumber.setDataFormat(format);
        }

        Cell cell = row.createCell(COLUMN_INDEX_ID);
        cell.setCellValue(book.getId());

        cell = row.createCell(COLUMN_INDEX_TITLE);
        cell.setCellValue(book.getTitle());

        cell = row.createCell(COLUMN_INDEX_AUTHOR);
        cell.setCellValue(book.getAuthor());

        cell = row.createCell(COLUMN_INDEX_PUBLISHED);
        cell.setCellValue(book.getPublished() != null ? book.getPublished() : 0);

        cellStyleFormatNumber.setDataFormat((short) BuiltinFormats.getBuiltinFormat("d-mmm-yy"));
        cell = row.createCell(COLUMN_INDEX_IMPORTED);
        cell.setCellStyle(cellStyleFormatNumber);
        cell.setCellValue(book.getImported() != null ? book.getImported() : new Date(new java.util.Date().getTime()));

        // Create cell formula
        // totalMoney = price * quantity
        // cell = row.createCell(COLUMN_INDEX_TOTAL, CellType.FORMULA);
        // cell.setCellStyle(cellStyleFormatNumber);
        // int currentRow = row.getRowNum() + 1;
        // String columnPrice = CellReference.convertNumToColString(COLUMN_INDEX_PRICE);
        // String columnQuantity =
        // CellReference.convertNumToColString(COLUMN_INDEX_QUANTITY);
        // cell.setCellFormula(columnPrice + currentRow + "*" + columnQuantity +
        // currentRow);
    }

    // Create CellStyle for header
    private static CellStyle createStyleForHeader(Sheet sheet) {
        // Create font
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
        font.setBold(true);
        font.setFontHeightInPoints((short) 14); // font size
        font.setColor(IndexedColors.WHITE.getIndex()); // text color

        // Create CellStyle
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        return cellStyle;
    }

    // Write footer
    // private static void writeFooter(Sheet sheet, int rowIndex) {
    // // Create row
    // Row row = sheet.createRow(rowIndex);
    // Cell cell = row.createCell(COLUMN_INDEX_TOTAL, CellType.FORMULA);
    // cell.setCellFormula("SUM(E2:E6)");
    // }

    // Auto resize column width
    private static void autosizeColumn(Sheet sheet, int lastColumn) {
        for (int columnIndex = 0; columnIndex < lastColumn; columnIndex++) {
            sheet.autoSizeColumn(columnIndex);
        }
    }

}
