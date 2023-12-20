package com.example.security;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFailureHandlerImpl implements AuthenticationFailureHandler {
    private Logger logger = Logger.getLogger(AuthenticationFailureHandlerImpl.class.getName());

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
            HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        logger.warning(">>> Exception: " + exception.getMessage());
        String redirectUrl = "/login?error";
        if (exception.getMessage().contains("user")) {
            logger.warning(">>> Username error");
            redirectUrl = "/login?authError=username";
        } else if (exception.getMessage().contains("credentials")) {
            logger.warning(">>> Password error");
            logger.warning(">>> Username:" + request.getParameter("username"));
            redirectUrl = "/login?authError=password&username=" + request.getParameter("username");
        }
        response.sendRedirect(redirectUrl);
        // super.setDefaultFailureUrl(redirectUrl);
        // super.onAuthenticationFailure(request, response, exception);
    }
}
