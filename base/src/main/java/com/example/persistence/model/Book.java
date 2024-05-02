/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.persistence.model;

import java.sql.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table("book")
public class Book {

    @Id
    private long id;

    @NotBlank
    private String title;

    @NotBlank
    private String author;

    private Date imported;

    private Integer published;

    private Long libraryId;

    private Boolean deleted;

    @Override
    public String toString() {
        return this.author + ":" + this.title + "-" + this.published + "-" + this.libraryId;
    }
}