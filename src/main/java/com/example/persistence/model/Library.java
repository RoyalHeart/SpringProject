package com.example.persistence.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;

import jp.sf.amateras.mirage.annotation.Column;
import jp.sf.amateras.mirage.annotation.PrimaryKey;
import jp.sf.amateras.mirage.annotation.PrimaryKey.GenerationType;
import jp.sf.amateras.mirage.annotation.Table;
import lombok.Data;
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
@Data
@Table(name = "LIBRARY")
public class Library implements Serializable {

    @Id
    @PrimaryKey(generationType = GenerationType.SEQUENCE, generator = "SEQ_LIBRARY")
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;
}
