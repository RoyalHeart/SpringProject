package com.example;

import static org.junit.Assert.assertNotNull;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import org.junit.Test;

import com.example.service.API;

public class TestApi {
    private static Logger logger = Logger.getLogger(TestApi.class.getName());

    @Test
    public void testFetch() throws MalformedURLException, URISyntaxException {
        logger.info(">>> fetch openlibrary");
        String openlibraryTrendingUrl = "https://openlibrary.org/trending/now.json";
        Object response = API.fetch(new URI(openlibraryTrendingUrl).toURL());
        logger.info(response.toString());
        assertNotNull(response);
    }
}
