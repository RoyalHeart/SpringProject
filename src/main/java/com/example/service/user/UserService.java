package com.example.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.persistence.mirageRepo.UserDetailRepository;
import com.example.persistence.model.UserDetail;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserDetailRepository userDetailRepository;

    public UserDetail save(UserDetail newUser) {
        return userDetailRepository.save(newUser);
    }

    public void insert(UserDetail newUser) {
        userDetailRepository.insert(newUser);
    }

}
