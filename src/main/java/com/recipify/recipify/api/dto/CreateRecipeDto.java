package com.recipify.recipify.api.dto;

import com.recipify.recipify.data.entities.Recipe;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateRecipeDto(
        @NotEmpty(message = "Recipe name not provided")
        @Size(max = 50, message = "Recipe name must be between 0 and 50 characters long.")
        String name,

        @NotEmpty(message = "Recipe description not provided")
        @Size(max = 512, message = "Recipe name must be between 0 and 512 characters long.")
        String description,

        @NotNull(message = "Ingredients are missing")
        Set<String> ingredients
) {
        public Recipe toEntity() {
                Recipe recipe = new Recipe();
                recipe.setName(this.name);
                recipe.setDescription(this.description);
                return recipe;
        }
}
