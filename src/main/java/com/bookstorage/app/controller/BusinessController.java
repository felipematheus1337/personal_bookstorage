package com.bookstorage.app.controller;

import com.bookstorage.app.dto.ResponseDTO;
import com.bookstorage.app.dto.UserCreateDTO;
import com.bookstorage.app.service.BusinessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/user")
public class BusinessController {

    private final BusinessService businessService;

    public BusinessController(BusinessService service) {
        this.businessService = service;
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseDTO> create(@RequestBody UserCreateDTO dto) {

        var response = this.businessService.createUser(dto);

        return ResponseEntity.status(response.httpStatus()).body(response);
    }
}
