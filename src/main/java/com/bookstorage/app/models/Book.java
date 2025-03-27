package com.bookstorage.app.models;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String author;

    private String review;

    private String photoURL;

    private BookStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


}
