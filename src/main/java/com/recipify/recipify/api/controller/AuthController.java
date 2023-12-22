package com.recipify.recipify.api.controller;

import com.recipify.recipify.api.dto.UserRegisterDto;
import com.recipify.recipify.security.AuthenticationService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication API", description = "Endpoints for user authentication")
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/login")
    @Operation(summary = "User login", description = "Performs user login")
    public Map<String, String> login(@RequestParam String email, @RequestParam String password) {
        return Map.of("token", authenticationService.login(email, password));
    }

    @PostMapping("/register")
    @Operation(summary = "User registration | Hunter validation | Fetches Clearbit user information",
            description = "Performs user registration")
    public void register(@Valid @RequestBody UserRegisterDto userRegisterDto) {
        authenticationService.register(userRegisterDto);
    }

}
