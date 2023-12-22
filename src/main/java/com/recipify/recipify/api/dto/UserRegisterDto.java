package com.recipify.recipify.api.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UserRegisterDto(
        @NotEmpty(message = "Email is missing")
        @Size(max = 50, message = "Email must be between 0 and 50 characters long.")
        String email,

        @NotEmpty(message = "Password is missing")
        @Size(max = 50, message = "Password must be between 0 and 50 characters long.")
        String password
) {

}
