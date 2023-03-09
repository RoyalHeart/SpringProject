package com.example.persistence.mirageRepo;

import org.springframework.data.repository.query.Param;

import com.example.persistence.model.UserDetail;
import com.example.service.database.DbRepository;

public interface UserDetailRepository extends DbRepository<UserDetail, Long> {
    public UserDetail getUserByUsername(@Param("username") String username);
}
