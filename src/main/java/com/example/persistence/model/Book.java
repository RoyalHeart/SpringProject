/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.persistence.model;

import java.sql.Date;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;

import jp.sf.amateras.mirage.annotation.Column;
import jp.sf.amateras.mirage.annotation.PrimaryKey;
import jp.sf.amateras.mirage.annotation.PrimaryKey.GenerationType;
import jp.sf.amateras.mirage.annotation.Table;
import lombok.Getter;
import lombok.Setter;

// JPA Entity
// @Entity
// @Getter
// @Setter
// @Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "title", "author" }) })
// public class Book {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private long id;

//     @Column(nullable = false)
//     @NotBlank
//     private String title;

//     @Column(nullable = false)
//     @NotBlank
//     private String author;

//     @Column(nullable = true)
//     private Date imported;

//     @Column(nullable = true, length = 4, columnDefinition = "smallint")
//     private Short published;

//     @Column(nullable = false, name = "libraryId")
//     private long libraryId;

//     @Override
//     public String toString() {
//         return this.author + ":" + this.title + "-" + this.published;
//     }
// }

@Table(name = "BOOK")
@Getter
@Setter
public class Book {

    @Id
    @Column(name = "id")
    @PrimaryKey(generationType = GenerationType.SEQUENCE, generator = "SEQ_BOOK")
    private long id;

    @Column(name = "title")
    @NotBlank
    private String title;

    @Column(name = "author")
    @NotBlank
    private String author;

    @Column(name = "imported")
    private Date imported;

    @Column(name = "published")
    private Short published;

    @Column(name = "library_id")
    private long libraryId;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @Override
    public String toString() {
        return this.author + ":" + this.title + "-" + this.published;
    }
}