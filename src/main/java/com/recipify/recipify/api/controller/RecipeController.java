package com.recipify.recipify.api.controller;

import com.recipify.recipify.api.dto.CreateRecipeDto;
import com.recipify.recipify.api.dto.RecipeDto;
import com.recipify.recipify.api.enumerator.IngredientsCountSortType;
import com.recipify.recipify.services.RatingService;
import com.recipify.recipify.services.RecipeService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/recipes")
@Tag(name = "Recipe API", description = "Endpoints for manipulating with recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;
    private final RatingService rateRecipe;

    @PostMapping
    public void createRecipe(@Valid @RequestBody CreateRecipeDto createRecipeDto) {
        recipeService.createRecipe(createRecipeDto);
    }

    @GetMapping("/{recipeId}/rating/{rating}")
    @Operation(summary = "Rate recipe", description = "Rate recipe from 1 to 5, cannot rate your own ones")
    public void rateRecipe(@PathVariable Integer recipeId,
                           @Min(value = 1, message = "Minimum value of rating is 1")
                           @Max(value = 5, message = "Maximum value of rating is 5")
                           @PathVariable Integer rating) {
        rateRecipe.rateRecipe(recipeId, rating);
    }

    @GetMapping
    @Operation(summary = "List all recipes | Supports pagination | " +
            "Search criteria by name, description or ingredients | " +
            "Sorting by LEAST or MOST ingredients")
    public List<RecipeDto> listRecipes(@RequestParam(defaultValue = "0") Integer page,
                                       @RequestParam(defaultValue = "10") Integer pageSize,
                                       @RequestParam(defaultValue = "", required = false) String searchText,
                                       @RequestParam(defaultValue = "LEAST") IngredientsCountSortType ingredientCount) {
        return recipeService.listRecipes(page, pageSize, searchText, ingredientCount);
    }

    @GetMapping("/owned")
    @Operation(summary = "List own recipes | Supports pagination", description = "List all recipes that you own")
    public List<RecipeDto> listOwnedRecipes(@RequestParam(defaultValue = "0") Integer page,
                                            @RequestParam(defaultValue = "10") Integer pageSize) {
        return recipeService.listOwnedRecipes(page, pageSize);
    }

}
