package com.example.service;

import com.example.persistence.model.UserDetail;

public interface UserDetailService {
    public UserDetail getUserByUsername(String username);
}
