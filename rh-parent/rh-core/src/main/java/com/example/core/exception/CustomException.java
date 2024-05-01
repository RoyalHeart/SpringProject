package com.example.core.exception;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

public class CustomException extends Exception {
    String code;
    String message;

    @Autowired
    private MessageSource messageSource;

    public CustomException(String message, Locale locale) {
        this.message = messageSource.getMessage(message, null, locale);
    }
}
