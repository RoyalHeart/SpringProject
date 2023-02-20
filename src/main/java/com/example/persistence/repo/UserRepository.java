package com.example.persistence.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.persistence.model.UserDetail;

public interface UserRepository extends CrudRepository<UserDetail, Long> {

    // @Query("SELECT u FROM User u WHERE u.username = :username")
    public UserDetail getUserByUsername(@Param("username") String username);
}
