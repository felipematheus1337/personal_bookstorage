package com.bookstorage.app.dto;

import com.bookstorage.app.models.BookStatus;

public record BookCreateDTO(String title, String author, String review, BookStatus status) {
}
