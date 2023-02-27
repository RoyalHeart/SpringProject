package com.example.persistence.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.persistence.model.UserDetail;

public interface UserRepository extends CrudRepository<UserDetail, Long> {

    public UserDetail getUserByUsername(@Param("username") String username);
}
