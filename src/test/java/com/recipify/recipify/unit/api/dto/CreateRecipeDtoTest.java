package com.recipify.recipify.unit.api.dto;

import com.recipify.recipify.api.dto.CreateRecipeDto;
import com.recipify.recipify.data.entities.Recipe;

import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CreateRecipeDtoTest {

    @Test
    public void toEntity() {

        CreateRecipeDto dto = new CreateRecipeDto("test", "lorem ipsum", new HashSet<>());

        Recipe entity = dto.toEntity();

        assertNotNull(entity);
        assertEquals(dto.name(), entity.getName());
        assertEquals(dto.description(), entity.getDescription());

    }

}
