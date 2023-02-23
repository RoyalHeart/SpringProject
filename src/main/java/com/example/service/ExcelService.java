package com.example.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ExcelService {

    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String[] HEADERs = { "Id", "Title", "Description", "Published" };

    public static boolean isXLSX(MultipartFile file) {
        if (TYPE.equals(file.getContentType())) {
            return true;
        }
        return false;
    }

}
