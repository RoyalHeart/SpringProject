package com.example.service;

import org.springframework.context.annotation.Primary;

import com.example.persistence.model.UserDetail;

public interface UserDetailService {
    public UserDetail getUserByUsername(String username);
}
