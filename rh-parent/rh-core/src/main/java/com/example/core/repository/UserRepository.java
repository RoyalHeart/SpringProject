package com.example.core.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.core.entity.UserDetail;

public interface UserRepository extends CrudRepository<UserDetail, Long> {

    public UserDetail findByUsername(@Param("username") String username);
}
