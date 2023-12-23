package com.recipify.recipify.integration.data.repositories;

import com.recipify.recipify.api.dto.IngredientUsageDto;
import com.recipify.recipify.data.entities.RecipeIngredient;
import com.recipify.recipify.data.repositories.RecipeIngredientsRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("classpath:application.yml")
public class RecipeIngredientsRepositoryTest {

    @Autowired
    private RecipeIngredientsRepository repository;

    @Test
    public void findIngredientsByRecipeIds_multipleRecipes() {
        List<RecipeIngredient> result = repository.findIngredientsByRecipeIds(Set.of(1, 2));

        assertFalse(result.isEmpty());
        assertEquals(7, result.size());
    }

    @Test
    public void findIngredientsByRecipeIds_singleRecipe() {
        List<RecipeIngredient> result = repository.findIngredientsByRecipeIds(Set.of(1));

        assertFalse(result.isEmpty());
        assertEquals(4, result.size());
    }

    @Test
    public void findIngredientsByRecipeIds_notFound() {
        List<RecipeIngredient> result = repository.findIngredientsByRecipeIds(Set.of(3));

        assertTrue(result.isEmpty());
    }

    @Test
    public void find5MostUsedIngredients() {
        List<IngredientUsageDto> result = repository.find5MostUsedIngredients();

        assertFalse(result.isEmpty());

        IngredientUsageDto top1Item = result.get(0);
        IngredientUsageDto top5Item = result.get(4);

        assertEquals("salt", top1Item.name());
        assertEquals(2L, top1Item.timesUsed());
        assertTrue(top1Item.timesUsed().intValue() >= top5Item.timesUsed().intValue());

    }

}
