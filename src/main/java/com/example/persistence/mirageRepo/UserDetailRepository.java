package com.example.persistence.mirageRepo;

import org.springframework.data.repository.query.Param;

import com.example.persistence.model.UserDetail;
import com.example.service.database.DbRepository;

import jp.xet.springframework.data.mirage.repository.query.Modifying;

public interface UserDetailRepository extends DbRepository<UserDetail, Long> {
    public UserDetail getUserByUsername(@Param("username") String username);

    @Modifying
    public void insert(@Param("userDetail") UserDetail userDetail);
}
