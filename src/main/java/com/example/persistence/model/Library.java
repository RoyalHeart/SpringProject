package com.example.persistence.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Getter;
import lombok.Setter;

// @Entity
// @Data
// public class Library implements Serializable {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private long id;

//     @Column(nullable = false)
//     private String name;
// }

@Getter
@Setter
@Table("LIBRARY")
public class Library {

    @Id
    private long id;

    private String name;
}
