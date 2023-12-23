package com.recipify.recipify.unit.api.dto;

import com.recipify.recipify.api.dto.RecipeDto;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RecipeDtoTest {

    @Test
    public void addIngredients() {
        RecipeDto dto = new RecipeDto(1, "test", "lorem ipsum", "owner", 5.0);

        RecipeDto dtoWithIngredients = dto.addIngredients(Set.of("ingredient1", "ingredient2"));

        assertNotNull(dtoWithIngredients);
        assertEquals(dto.id(), dtoWithIngredients.id());
        assertEquals(dto.name(), dtoWithIngredients.name());
        assertEquals(dto.description(), dtoWithIngredients.description());
        assertEquals(dto.ownerName(), dtoWithIngredients.ownerName());
        assertEquals(dto.rating(), dtoWithIngredients.rating());
        assertEquals(2, dtoWithIngredients.ingredients().size());

    }

}
