package com.bookstorage.app.controller;

import com.bookstorage.app.dto.BookCreateDTO;
import com.bookstorage.app.dto.ResponseDTO;
import com.bookstorage.app.dto.UserCreateDTO;
import com.bookstorage.app.service.BusinessService;
import com.bookstorage.app.utils.BusinessHelperUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("api/v1")
public class BusinessController {

    private final BusinessService businessService;
    private final ObjectMapper objectMapper;
    private static final Logger log = LoggerFactory.getLogger(BusinessController.class);

    public BusinessController(BusinessService service, ObjectMapper objectMapper) {
        this.businessService = service;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/user/create")
    public ResponseEntity<ResponseDTO> create(@RequestBody UserCreateDTO dto) {

        var response = this.businessService.createUser(dto);

        return ResponseEntity.status(response.httpStatus()).body(response);
    }

    @PostMapping("/book/create/{userId}")
    public ResponseEntity<Void> registerBooks(@PathVariable(name = "userId") Long userId,
                                              @RequestParam("metadata") String metadataJSON,
                                              @RequestParam("file") MultipartFile file) {

        try {
            BookCreateDTO dto = this.objectMapper.readValue(metadataJSON, BookCreateDTO.class);
            String fileName = BusinessHelperUtil.normalizeFileName(file.getOriginalFilename(), "123");
            File tempFile = File.createTempFile("temp", fileName);
        } catch (JsonProcessingException e) {
            log.error("Erro ao processar JSON: {}", e.getMessage(), e);
            throw new RuntimeException("Falha ao processar JSON", e);
        } catch (IOException e) {
            log.error("Erro de I/O ao criar arquivo temporário: {}", e.getMessage(), e);
            throw new RuntimeException("Falha ao manipular arquivo", e);
        }



        return null;
    }
}
