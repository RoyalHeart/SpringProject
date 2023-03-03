package com.example;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.example.security.Validate;

public class TestValidation {
    
    @Test
    public void usernameValidation(){
        List<String> wrongUsernames = new ArrayList<String>();
        wrongUsernames.add("a");
        wrongUsernames.add(".");
        wrongUsernames.add(".hello");
        wrongUsernames.add("hello.");
        wrongUsernames.add("hello..");
        wrongUsernames.add("hello-");
        wrongUsernames.add("hel--lo");
        wrongUsernames.add("hello_");
        wrongUsernames.add("hell__o");
        wrongUsernames.add("hell._o");
        wrongUsernames.add("hell.-o");
        List<String> rightUsernames = new ArrayList<String>();
        rightUsernames.add("hello");
        rightUsernames.add("he.llo");
        rightUsernames.add("he-llo");
        rightUsernames.add("he_llo");
        for(String username : wrongUsernames){
            assertFalse(Validate.isUsernameValid(username));
        }
        for(String username : rightUsernames){
            assertTrue(Validate.isUsernameValid(username));
        }
    }
}
