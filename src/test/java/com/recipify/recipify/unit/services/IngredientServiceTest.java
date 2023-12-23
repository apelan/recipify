package com.recipify.recipify.unit.services;

import com.recipify.recipify.api.dto.IngredientUsageDto;
import com.recipify.recipify.api.dto.RecipeDto;
import com.recipify.recipify.data.entities.Ingredient;
import com.recipify.recipify.data.repositories.IngredientRepository;
import com.recipify.recipify.data.repositories.RecipeIngredientsRepository;
import com.recipify.recipify.services.IngredientService;
import com.recipify.recipify.unit.CommonMockObjects;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class IngredientServiceTest {

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private RecipeIngredientsRepository recipeIngredientsRepository;

    @InjectMocks
    private IngredientService service;

    @Captor
    private ArgumentCaptor<Ingredient> ingredientArgumentCaptor;


    @Test
    public void checkExistingIngredients_allExists() {
        // GIVEN
        List<Ingredient> ingredients = List.of(
                CommonMockObjects.getMockIngredient("ingredient1"),
                CommonMockObjects.getMockIngredient("ingredient2")
        );

        Set<String> ingredientNames = Set.of("ingredient1", "ingredient2");
        when(ingredientRepository.findByNameIn(ingredientNames)).thenReturn(ingredients);

        // WHEN
        List<Ingredient> result = service.checkExistingIngredients(ingredientNames);

        // THEN
        assertEquals(2, result.size());

    }

    @Test
    public void checkExistingIngredients_oneDoesNotExist_mustSaveIt() {
        // GIVEN
        List<Ingredient> ingredients = List.of(
                CommonMockObjects.getMockIngredient("ingredient1")
        );

        Set<String> ingredientNames = Set.of("ingredient1", "ingredient2");
        when(ingredientRepository.findByNameIn(ingredientNames)).thenReturn(ingredients);

        // WHEN
        List<Ingredient> result = service.checkExistingIngredients(ingredientNames);

        // THEN
        assertEquals(2, result.size());
        verify(ingredientRepository).save(ingredientArgumentCaptor.capture());

    }

    @Test
    public void attachIngredientToRecipes_noIngredientsToAdd() {
        // GIVEN
        RecipeDto recipe = new RecipeDto(1, "test", "test", "test", 5.0);
        when(recipeIngredientsRepository.findIngredientsByRecipeIds(Set.of(1)))
                .thenReturn(Collections.emptyList());

        // WHEN
        List<RecipeDto> result = service.attachIngredientToRecipes(List.of(recipe));

        // THEN
        assertEquals(1, result.size());
        assertEquals(recipe.id(), result.get(0).id());
        assertTrue(result.get(0).ingredients().isEmpty());

    }

    @Test
    public void attachIngredientToRecipes_ingredientsAddedSuccessfully() {
        // GIVEN
        RecipeDto recipe = new RecipeDto(1, "test", "test", "test", 5.0);
        when(recipeIngredientsRepository.findIngredientsByRecipeIds(Set.of(1)))
                .thenReturn(List.of(CommonMockObjects.getMockRecipeIngredient()));

        // WHEN
        List<RecipeDto> result = service.attachIngredientToRecipes(List.of(recipe));

        // THEN
        assertEquals(1, result.size());
        assertEquals(recipe.id(), result.get(0).id());
        assertFalse(result.get(0).ingredients().isEmpty());
        assertEquals("test-ingredient1", result.get(0).ingredients().iterator().next());

    }

    @Test
    public void top5MostUsed() {
        // GIVEN
        when(recipeIngredientsRepository.find5MostUsedIngredients())
                .thenReturn(CommonMockObjects.getMockListIngredientUsageDto());

        // WHEN
        List<IngredientUsageDto> result = service.top5MostUsed();

        // THEN
        assertEquals(5, result.size());
        assertEquals("test-ingredient1", result.get(0).name());
        assertEquals("test-ingredient5", result.get(4).name());

    }


}
