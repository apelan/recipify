package com.recipify.recipify.unit;

import com.recipify.recipify.api.dto.CreateRecipeDto;
import com.recipify.recipify.api.dto.IngredientUsageDto;
import com.recipify.recipify.api.dto.RecipeDto;
import com.recipify.recipify.data.entities.Ingredient;
import com.recipify.recipify.data.entities.Recipe;
import com.recipify.recipify.data.entities.RecipeIngredient;
import com.recipify.recipify.data.entities.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.Set;

public class CommonMockObjects {

    public static User getMockUser() {
        User user = new User();
        user.setId(1);
        user.setEmail("test@test.com");
        user.setPassword("test");
        return user;
    }

    public static Ingredient getMockIngredient(String name) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(name);
        return ingredient;
    }

    public static Recipe getMockRecipe() {
        Recipe recipe = new Recipe();
        recipe.setId(1);
        recipe.setName("test-recipe");
        recipe.setDescription("test-description");
        recipe.setOwner(getMockUser());
        return recipe;
    }

    public static RecipeIngredient getMockRecipeIngredient() {
        Recipe recipe = getMockRecipe();
        Ingredient ingredient = getMockIngredient("test-ingredient1");
        return new RecipeIngredient(recipe, ingredient);
    }

    public static List<IngredientUsageDto> getMockListIngredientUsageDto() {
        return List.of(
                new IngredientUsageDto("test-ingredient1", 150),
                new IngredientUsageDto("test-ingredient2", 136),
                new IngredientUsageDto("test-ingredient3", 92),
                new IngredientUsageDto("test-ingredient4", 91),
                new IngredientUsageDto("test-ingredient5", 77)
        );
    }

    public static CreateRecipeDto getMockCreateRecipeDto() {
        return new CreateRecipeDto("test-recipe", "test-description",
                Set.of("test-ingredient1", "test-ingredient2"));
    }

    public static RecipeDto getMockRecipeDto() {
        return new RecipeDto(1, "test name", "test description", "test owner", 5.0);
    }

    public static Page<RecipeDto> getMockPageRecipeDto() {
        return new PageImpl<>(List.of(getMockRecipeDto()));
    }

}
