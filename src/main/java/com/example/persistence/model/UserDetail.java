package com.example.persistence.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.example.annotation.ValidUsername;

import lombok.Data;

@Entity
@Data
public class UserDetail implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    @ValidUsername
    private String username;

    @Column
    private String user_password;

    @Column
    private String user_role;

    @Override
    public String toString() {
        return id + ": " + username + "-" + user_password + user_role;
    }
}
