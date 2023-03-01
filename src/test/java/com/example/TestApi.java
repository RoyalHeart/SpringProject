package com.example;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import com.example.service.API;

public class TestApi {
    private static Logger logger = Logger.getLogger(TestApi.class.getName());
    @Test
    public void testFetch() throws MalformedURLException{
        Object response = API.fetch(new URL("https://openlibrary.org/trending/now.json"));
        logger.info(response.toString());
        assertTrue( response != null);
    } 
}
