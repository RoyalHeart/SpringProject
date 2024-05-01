package com.example.core.exception;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

public class CustomException extends Exception {
    final String code;
    final String message;

    @Autowired
    private MessageSource messageSource;

    public CustomException(String message, Locale locale) {
        this.code = "";
        this.message = messageSource.getMessage(message, null, locale);
    }

    public CustomException(String code, String message, Locale locale) {
        this.code = code;
        this.message = messageSource.getMessage(message, null, locale);
    }
}
