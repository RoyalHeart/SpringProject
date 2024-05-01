package com.example.core.user.entity;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.example.core.annotation.ValidUsername;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table("user_detail")
public class UserDetail implements Serializable {
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
