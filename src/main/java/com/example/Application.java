package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

// @EnableJpaRepositories("com.example.persistence.repo")
// @EnableMirageRepositories("com.example.persistence.mirageRepo")
// @EntityScan("com.example.persistence.model")
@SpringBootApplication(scanBasePackages = { "com.example.config", "com.example.controller",
        "com.example.security", "com.example.service",
        "com.example.service.database" })
// @SpringBootApplication(scanBasePackages = { "com.example" })
@EnableScheduling
public class Application extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Application.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
