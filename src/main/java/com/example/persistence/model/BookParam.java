package com.example.persistence.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookParam extends Book {
    private short from;
    private short to;
}