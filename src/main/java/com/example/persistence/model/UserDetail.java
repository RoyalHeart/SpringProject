package com.example.persistence.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;

import com.example.annotation.ValidUsername;

import jp.sf.amateras.mirage.annotation.Column;
import jp.sf.amateras.mirage.annotation.PrimaryKey;
import jp.sf.amateras.mirage.annotation.PrimaryKey.GenerationType;
import jp.sf.amateras.mirage.annotation.Table;
import lombok.Getter;
import lombok.Setter;

// @Entity
// @Data
// public class UserDetail implements Serializable {
//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private long id;

//     @Column(nullable = false, unique = true)
//     @ValidUsername
//     private String username;

//     @Column
//     private String user_password;

//     @Column
//     private String user_role;

//     @Override
//     public String toString() {
//         return id + ": " + username + "-" + user_password + user_role;
//     }
// }
@Getter
@Setter
@Table(name = "USER_DETAIL")
public class UserDetail implements Serializable {
    @Id
    @PrimaryKey(generationType = GenerationType.SEQUENCE, generator = "SEQ_USER_DETAIL")
    private long id;

    @Column(name = "username")
    @ValidUsername
    private String username;

    @Column(name = "user_password")
    private String user_password;

    @Column(name = "user_role")
    private String user_role;

    @Override
    public String toString() {
        return id + ": " + username + "-" + user_password + user_role;
    }
}
