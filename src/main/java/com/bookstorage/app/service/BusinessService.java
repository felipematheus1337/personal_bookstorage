package com.bookstorage.app.service;

import com.bookstorage.app.dto.BookCreateDTO;
import com.bookstorage.app.dto.ResponseDTO;
import com.bookstorage.app.dto.UserCreateDTO;

import java.io.File;
import java.io.IOException;

public interface BusinessService {

    ResponseDTO createUser(UserCreateDTO dto);
    void registerBook(File file, Long userId, BookCreateDTO dto) throws IOException;
}
