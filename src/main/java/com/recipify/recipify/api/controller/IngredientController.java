package com.recipify.recipify.api.controller;

import com.recipify.recipify.api.dto.IngredientUsageDto;
import com.recipify.recipify.services.IngredientService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/ingredients")
@Tag(name = "Ingredients API", description = "Endpoints for manipulating with ingredients")
@RequiredArgsConstructor
public class IngredientController {

    private final IngredientService ingredientService;

    @GetMapping("/mostUsed")
    @Operation(summary = "Top 5 most used ingredients")
    public List<IngredientUsageDto> top5MostUsed() {
        return ingredientService.top5MostUsed();
    }

}
