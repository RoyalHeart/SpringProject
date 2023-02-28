package com.example.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ExcelService {
    private static Logger logger = Logger.getLogger(ExcelService.class.getName());

    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String[] HEADERs = { "Id", "Title", "Description", "Published" };

    public static boolean isXLSX(MultipartFile file) {
        if (TYPE.equals(file.getContentType())) {
            return true;
        }
        return false;
    }

    public static void createOutputFile(Workbook workbook, String excelFilePath) throws IOException {
        try (OutputStream os = new FileOutputStream(excelFilePath)) {
            workbook.write(os);
            logger.info(">>> Exported at:" + excelFilePath);
        }
    }

    public static Workbook loadBook(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);
            return workbook;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }
}
