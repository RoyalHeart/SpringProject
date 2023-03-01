package com.example.service.database;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties("spring.datasource")
@Data
public class DatabaseConfiguration {
    private String url;
    private String username;
    private String password;
}
