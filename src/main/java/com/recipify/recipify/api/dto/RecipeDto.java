package com.recipify.recipify.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashSet;
import java.util.Set;

public record RecipeDto(
        @JsonIgnore
        Integer id,
        String name,
        String description,
        String ownerName,
        Integer rating,
        Set<String> ingredients
) {

    public RecipeDto(Integer id, String name, String description, String ownerName, Double rating) {
        this(id, name, description, ownerName, rating != null ? rating.intValue() : null, new HashSet<>());
    }

    public RecipeDto addIngredients(Set<String> ingredients) {
        return new RecipeDto(this.id, this.name, this.description, this.ownerName, this.rating, ingredients);
    }

}
