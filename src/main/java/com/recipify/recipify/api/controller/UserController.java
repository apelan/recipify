package com.recipify.recipify.api.controller;

import com.recipify.recipify.api.dto.UserInfoDto;
import com.recipify.recipify.services.UserDetailsService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Users API", description = "Endpoints for manipulating with users")
@RequiredArgsConstructor
public class UserController {

    private final UserDetailsService userDetailsService;

    @GetMapping
    @Operation(summary = "User info", description = "Returns information about current user")
    public UserInfoDto userInfo() {
        return userDetailsService.getUserInfo();
    }

}
