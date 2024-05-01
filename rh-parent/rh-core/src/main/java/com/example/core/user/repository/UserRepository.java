package com.example.core.user.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.core.user.entity.UserDetail;

@Repository
public interface UserRepository extends CrudRepository<UserDetail, Long> {

    public UserDetail findByUsername(@Param("username") String username);
}
