package com.example.security;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AuthenticationFailureHandlerImpl implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
            HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        log.error(">>> Exception: " + exception.getMessage());
        String redirectUrl = "/login?error";
        if (exception.getMessage().contains("user")) {
            log.error(">>> Username error");
            redirectUrl = "/login?authError=username";
        } else if (exception.getMessage().contains("credentials")) {
            log.error(">>> Password error");
            log.error(">>> Username:" + request.getParameter("username"));
            redirectUrl = "/login?authError=password&username=" + request.getParameter("username");
        }
        response.sendRedirect(redirectUrl);
    }
}
