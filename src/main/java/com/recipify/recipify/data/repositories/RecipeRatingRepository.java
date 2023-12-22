package com.recipify.recipify.data.repositories;

import com.recipify.recipify.data.entities.RecipeRating;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecipeRatingRepository extends JpaRepository<RecipeRating, Integer> {

    Optional<RecipeRating> findByRecipeIdAndUserId(Integer recipeId, Integer userId);

}
