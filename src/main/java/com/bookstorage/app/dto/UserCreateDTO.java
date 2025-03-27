package com.bookstorage.app.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record UserCreateDTO(@NotNull @NotBlank String name,
                           @Email String email,
                           @Length(min = 6) String password) {
}
