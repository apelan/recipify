package com.recipify.recipify.data.repositories;

import com.recipify.recipify.api.dto.RecipeDto;
import com.recipify.recipify.data.entities.Recipe;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Integer> {

    @Query(value = "SELECT new com.recipify.recipify.api.dto.RecipeDto(r.id, " +
            "r.name, r.description, CONCAT(u.firstName, ' ', u.lastName) AS ownerName, " +
            "ROUND(AVG(rr.rating)) as rating), COUNT(ri.ingredient.id) AS ingredientCount " +
            "FROM Recipe r " +
            "LEFT JOIN RecipeIngredient ri ON r.id = ri.recipe.id " +
            "LEFT JOIN RecipeRating rr ON r.id = rr.recipe.id " +
            "LEFT JOIN User u ON r.owner.id = u.id " +
            "WHERE :text IS NULL " +
            "OR r.name LIKE CONCAT('%', :text, '%') " +
            "OR r.description LIKE CONCAT('%', :text, '%') " +
            "OR EXISTS (" +
            "   SELECT 1 FROM Ingredient i " +
            "   JOIN RecipeIngredient ri2 ON i.id = ri2.ingredient.id " +
            "   WHERE r.id = ri2.recipe.id AND i.name LIKE CONCAT('%', :text, '%')) " +
            "GROUP BY r.id, r.name, r.description, ownerName ",
            countQuery = "SELECT COUNT(DISTINCT r.id) FROM Recipe r " +
                    "LEFT JOIN RecipeIngredient ri ON r.id = ri.recipe.id " +
                    "LEFT JOIN RecipeRating rr ON r.id = rr.recipe.id " +
                    "LEFT JOIN User u ON r.owner.id = u.id")
    Page<RecipeDto> findAllRecipes(@Param("text") String text, Pageable pageable);

    @Query(value = "SELECT new com.recipify.recipify.api.dto.RecipeDto(r.id, " +
            "r.name, r.description, CONCAT(u.firstName, ' ', u.lastName) AS ownerName, " +
            "ROUND(AVG(rr.rating)) as rating) " +
            "FROM Recipe r " +
            "LEFT JOIN RecipeRating rr ON r.id = rr.recipe.id " +
            "LEFT JOIN User u ON r.owner.id = u.id " +
            "WHERE u.id = :userId " +
            "GROUP BY r.id, r.name, r.description, ownerName",
            countQuery = "SELECT COUNT(DISTINCT r.id) FROM Recipe r " +
                    "LEFT JOIN RecipeRating rr ON r.id = rr.recipe.id " +
                    "LEFT JOIN User u ON r.owner.id = u.id")
    Page<RecipeDto> findAllOwnedRecipes(@Param("userId") Integer userId, Pageable pageable);

}
