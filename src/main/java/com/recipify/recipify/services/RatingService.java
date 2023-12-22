package com.recipify.recipify.services;

import com.recipify.recipify.data.entities.Recipe;
import com.recipify.recipify.data.entities.RecipeRating;
import com.recipify.recipify.data.entities.User;
import com.recipify.recipify.data.repositories.RecipeRatingRepository;
import com.recipify.recipify.data.repositories.RecipeRepository;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class RatingService {

    private final RecipeRatingRepository recipeRatingRepository;
    private final UserDetailsService userService;
    private final RecipeRepository recipeRepository;

    /**
     * Method used to rate recipe. If current user already rated that recipe we just update value.
     *
     * @param recipeId recipe identifier to rate
     * @param rating   integer 1-5
     */
    @Transactional
    public void rateRecipe(Integer recipeId, Integer rating) {
        log.info("Rating recipe {} with rating {}", recipeId, rating);
        User user = userService.currentUser();

        // check if recipe exists
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> {
                    String message = String.format("Recipe with id %s doesn't exist", recipeId);
                    log.error(message);
                    return new ValidationException(message);
                });

        // cannot rate own recipes
        if (user.getId().equals(recipe.getOwner().getId())) {
            String message = String.format("Cannot rate your own recipe: %s", recipe.getName());
            log.error(message);
            throw new ValidationException(message);
        }

        // return existing rating by user and update it, otherwise create new rating
        RecipeRating recipeRating = recipeRatingRepository.findByRecipeIdAndUserId(recipeId, user.getId())
                .orElse(new RecipeRating(user, recipe));
        recipeRating.setRating(rating);
        recipeRatingRepository.save(recipeRating);

    }

}
