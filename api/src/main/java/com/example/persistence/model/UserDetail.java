package com.example.persistence.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.example.security.annotation.ValidUsername;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table("user_detail")
public class UserDetail {
    @Id
    private long id;

    @ValidUsername
    private String username;

    private String password;

    private String role;

    @Override
    public String toString() {
        return id + ": " + username + "-" + password + role;
    }
}
