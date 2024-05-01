package com.example.core.i18;

import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.example.core.constant.BaseConstant;

@Configuration
public class I18nConfiguration {
    @Bean(name = "messageSource")
    public ResourceBundleMessageSource getMessageResource() {
        ResourceBundleMessageSource rs = new ResourceBundleMessageSource();
        rs.setBasenames("i18n/error_messages");
        rs.setDefaultEncoding(BaseConstant.UTF_8);
        rs.setUseCodeAsDefaultMessage(true);
        return rs;
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.of(BaseConstant.DEFAULT_LANGUAGE));
        return slr;
    }
}
