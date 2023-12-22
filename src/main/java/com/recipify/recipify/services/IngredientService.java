package com.recipify.recipify.services;

import com.recipify.recipify.api.dto.IngredientUsageDto;
import com.recipify.recipify.api.dto.RecipeDto;
import com.recipify.recipify.data.entities.Ingredient;
import com.recipify.recipify.data.entities.RecipeIngredient;
import com.recipify.recipify.data.repositories.IngredientRepository;
import com.recipify.recipify.data.repositories.RecipeIngredientsRepository;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class IngredientService {

    private final IngredientRepository ingredientRepository;
    private final RecipeIngredientsRepository recipeIngredientsRepository;

    /**
     * Method is used to check all passed ingredients against database,
     * it will persist all ingredients that are not already present.
     *
     * @param ingredients Set of Strings which represents Ingredient names
     * @return all ingredients (existing and newly created ones)
     */
    public List<Ingredient> checkExistingIngredients(Set<String> ingredients) {
        log.trace("Checking existing ingredients: {}", ingredients);

        List<Ingredient> existingIngredients = ingredientRepository.findByNameIn(ingredients);
        List<Ingredient> result = new ArrayList<>(existingIngredients);

        Set<String> existingIngredientNames = existingIngredients.stream()
                .map(Ingredient::getName)
                .collect(Collectors.toSet());

        for (String ingredient : ingredients) {
            if (!existingIngredientNames.contains(ingredient)) {
                // If no ingredient in database we save new one
                Ingredient ingredientToSave = new Ingredient();
                ingredientToSave.setName(ingredient);
                result.add(ingredientRepository.save(ingredientToSave));
                log.trace("Added new ingredient {}", ingredient);
            }
        }

        return result;
    }

    /**
     * Method used to find all ingredients for recipes, aggregates data and returns it back.
     *
     * @param recipes list of {@link RecipeDto}
     * @return list of {@link RecipeDto} with populated ingredients (if exists)
     */
    public List<RecipeDto> attachIngredientToRecipes(List<RecipeDto> recipes) {
        // Extract recipe ids
        Set<Integer> recipeIds = recipes.stream()
                .map(RecipeDto::id)
                .collect(Collectors.toSet());

        // Fetch all ingredients for recipes
        List<RecipeIngredient> allRecipeIngredients = recipeIngredientsRepository
                .findIngredientsByRecipeIds(recipeIds);

        // Aggregate ingredients with corresponding recipes
        return recipes.stream()
                .map(recipe -> recipe.addIngredients(allRecipeIngredients.stream()
                        .filter(ingredient -> ingredient.getRecipe().getId().equals(recipe.id()))
                        .map(RecipeIngredient::getIngredient)
                        .map(Ingredient::getName)
                        .collect(Collectors.toSet())))
                .toList();
    }


    /**
     * Used to return top 5 most used ingredients.
     *
     * @return list of {@link IngredientUsageDto}
     */
    public List<IngredientUsageDto> top5MostUsed() {
        return recipeIngredientsRepository.find5MostUsedIngredients();
    }
}
