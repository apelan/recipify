package com.recipify.recipify.services;

import com.recipify.recipify.api.dto.CreateRecipeDto;
import com.recipify.recipify.api.dto.RecipeDto;
import com.recipify.recipify.api.enumerator.IngredientsCountSortType;
import com.recipify.recipify.data.entities.Ingredient;
import com.recipify.recipify.data.entities.Recipe;
import com.recipify.recipify.data.entities.RecipeIngredient;
import com.recipify.recipify.data.entities.User;
import com.recipify.recipify.data.repositories.RecipeIngredientsRepository;
import com.recipify.recipify.data.repositories.RecipeRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final IngredientService ingredientService;

    private final UserDetailsService userDetailsService;

    private final RecipeIngredientsRepository recipeIngredientsRepository;

    /**
     * Method used to create new recipe.
     *
     * @param createRecipeDto {@link CreateRecipeDto}
     */
    @Transactional
    public void createRecipe(CreateRecipeDto createRecipeDto) {
        log.info("Creating recipe {}", createRecipeDto.name());

        Recipe recipe = createRecipeDto.toEntity();
        recipe.setOwner(userDetailsService.currentUser());
        recipeRepository.save(recipe);

        List<Ingredient> ingredients = ingredientService.checkExistingIngredients(createRecipeDto.ingredients());
        List<RecipeIngredient> recipeIngredients = ingredients.stream()
                .map(ingredient -> new RecipeIngredient(recipe, ingredient))
                .toList();
        recipeIngredientsRepository.saveAll(recipeIngredients);
    }


    /**
     * Method used to list all recipes, results are paginated.
     *
     * @param page which page number
     * @param pageSize how many elements to return
     * @param searchText search by name, description or ingredients
     * @param ingredientCount MIN / MAX - {@link IngredientsCountSortType}
     * @return list of {@link RecipeDto}
     */
    public List<RecipeDto> listRecipes(Integer page, Integer pageSize,
                                       String searchText, IngredientsCountSortType ingredientCount) {
        log.info("Fetching recipes | Page: {} | Page size: {} | Search criteria: {} | Sort by ingredients: {}",
                page, pageSize, searchText, ingredientCount);

        // Create page request
        Sort sort = switch (ingredientCount) {
            case LEAST -> Sort.by("ingredientCount").ascending();
            case MOST -> Sort.by("ingredientCount").descending();
        };

        Pageable pageable = PageRequest.of(page, pageSize, sort);

        // Fetch paginated recipes
        Page<RecipeDto> recipes = recipeRepository.findAllRecipes(searchText, pageable);

        return ingredientService.attachIngredientToRecipes(recipes.stream().toList());
    }

    /**
     * Method used to list all recipes owned by current user, results are paginated.
     *
     * @param page     - default 0
     * @param pageSize - default 10
     * @return list of {@link RecipeDto}
     */
    public List<RecipeDto> listOwnedRecipes(Integer page, Integer pageSize) {
        log.info("Fetching owned recipes | Page: {} | Page size: {}", page, pageSize);

        User user = userDetailsService.currentUser();

        // Fetch paginated owned recipes
        Page<RecipeDto> recipes = recipeRepository.findAllOwnedRecipes(user.getId(), PageRequest.of(page, pageSize));

        return ingredientService.attachIngredientToRecipes(recipes.stream().toList());
    }

}
