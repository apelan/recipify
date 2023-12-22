package com.recipify.recipify.data.repositories;

import com.recipify.recipify.api.dto.IngredientUsageDto;
import com.recipify.recipify.data.entities.RecipeIngredient;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface RecipeIngredientsRepository extends JpaRepository<RecipeIngredient, Integer> {

    @Query("SELECT ri FROM RecipeIngredient ri " +
            "WHERE ri.recipe.id IN (:recipeIds)")
    List<RecipeIngredient> findIngredientsByRecipeIds(Set<Integer> recipeIds);

    @Query("SELECT new com.recipify.recipify.api.dto.IngredientUsageDto(" +
                "ri.ingredient.name, COUNT(ri.ingredient.id) as timesUsed) " +
            "FROM RecipeIngredient ri " +
            "GROUP BY ri.ingredient.name " +
            "ORDER BY timesUsed DESC " +
            "LIMIT 5")
    List<IngredientUsageDto> find5MostUsedIngredients();

}
