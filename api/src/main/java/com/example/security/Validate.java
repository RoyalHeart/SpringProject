package com.example.security;

import java.util.regex.Pattern;

public class Validate {
    // start with alphanumeric characters,then either -._ but not consecutive or
    // alphanumeric and end with alphanumeric
    private static final String USERNAME_REGEX = "^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){0,18}[a-zA-Z0-9]$";

    private static final Pattern PATTERN = Pattern.compile(USERNAME_REGEX);

    public static boolean isUsernameValid(String username) {
        return PATTERN.matcher(username).matches();
    }
}
