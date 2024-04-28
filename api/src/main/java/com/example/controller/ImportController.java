package com.example.controller;

import java.io.IOException;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.service.book.impl.BookServiceImpl;
import com.example.service.export_import.ExcelService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ImportController {

    @Autowired
    private BookServiceImpl bookService;

    @PostMapping("/import")
    public String importBook(@RequestParam("file") MultipartFile file, HttpServletRequest request,
            RedirectAttributes redirectAttributes) throws IOException {
        String referer = request.getHeader("Referer");
        if (ExcelService.isXLSX(file)) {
            redirectAttributes.addFlashAttribute("isExcel", true);
            Workbook workBook = ExcelService.loadBook(file.getInputStream());
            try {
                bookService.importFromExcel(workBook);
                redirectAttributes.addFlashAttribute("importSuccessfully",
                        "Imported successfully");
            } catch (Exception e) {
                String errorOutputPath = System.getProperty("user.dir");
                String exportPath = errorOutputPath + "/ErrorExcel_" + new java.util.Date().getTime() + ".xlsx";
                ExcelService.createOutputFile(workBook, exportPath);
                log.error(">>> Error importing books: " + e.getMessage());
                redirectAttributes.addFlashAttribute("importError",
                        "Import has error - Export wrong Excel at: " + exportPath);
            }
        } else {
            log.error(">>> Not Excel");
            redirectAttributes.addFlashAttribute("isExcel", false);
        }
        return "redirect:" + referer;
    }
}
