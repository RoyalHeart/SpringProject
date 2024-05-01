package com.example.security.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.core.api.controller.AbstractRestController;
import com.example.core.api.res.BaseResponse;
import com.example.security.auth.req.LoginRequest;
import com.example.security.auth.res.LoginResponse;
import com.example.security.auth.res.RandomStuff;
import com.example.security.jwt.JwtService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController extends AbstractRestController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService tokenProvider;

    @PostMapping("/login")
    public BaseResponse authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        long start = System.currentTimeMillis();
        try {
            log.info(">>> Login: " + loginRequest.toString());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = tokenProvider.generateToken(loginRequest.getUsername());
            LoginResponse loginResponse = new LoginResponse(jwt);
            return responseHandler.handleSuccessRequest(loginResponse, start);
        } catch (AuthenticationException e) {
            return responseHandler.handleErrorRequest(e, null, start);
        } catch (Exception e) {
            return responseHandler.handleErrorRequest(e, null, start);
        }
    }

    @GetMapping("/random")
    public RandomStuff randomStuff() {
        return new RandomStuff("JWT Hợp lệ mới có thể thấy được message này");
    }

}
