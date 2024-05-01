package com.example.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties("custom")
public class CustomProperties {
    private final JWT jwt = new JWT();

    @Data
    public static class JWT {
        private int validTime = 0;
        private String base64Secret;
    }
}
