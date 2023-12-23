package com.recipify.recipify.unit.services;

import com.recipify.recipify.data.entities.Recipe;
import com.recipify.recipify.data.entities.RecipeRating;
import com.recipify.recipify.data.entities.User;
import com.recipify.recipify.data.repositories.RecipeRatingRepository;
import com.recipify.recipify.data.repositories.RecipeRepository;
import com.recipify.recipify.services.RatingService;
import com.recipify.recipify.services.UserDetailsService;
import com.recipify.recipify.unit.CommonMockObjects;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import jakarta.validation.ValidationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class RatingServiceTest {

    @Mock
    private RecipeRatingRepository recipeRatingRepository;

    @Mock
    private UserDetailsService userService;

    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private RatingService service;

    @Test
    public void rateRecipe_successfully() {
        // GIVEN
        User user = CommonMockObjects.getMockUser();
        user.setId(2);
        Recipe recipe = CommonMockObjects.getMockRecipe();
        RecipeRating recipeRating = new RecipeRating(user, recipe);
        when(userService.currentUser()).thenReturn(user);
        when(recipeRepository.findById(1)).thenReturn(Optional.of(recipe));
        when(recipeRatingRepository.findByRecipeIdAndUserId(recipe.getId(), user.getId()))
                .thenReturn(Optional.of(recipeRating));

        // WHEN
        service.rateRecipe(1, 5);

        // THEN
        assertEquals(5, recipeRating.getRating());
        verify(recipeRatingRepository).save(recipeRating);

    }

    @Test
    public void rateRecipe_validation_recipeDoesNotExist() {
        // GIVEN
        User user = CommonMockObjects.getMockUser();
        when(userService.currentUser()).thenReturn(user);
        when(recipeRepository.findById(123)).thenReturn(Optional.empty());

        // WHEN THEN
        assertThrows(ValidationException.class, () -> service.rateRecipe(123, 5));
    }

    @Test
    public void rateRecipe_validation_cannotRateOwnRecipe() {
        // GIVEN
        User user = CommonMockObjects.getMockUser();
        Recipe recipe = CommonMockObjects.getMockRecipe();
        when(userService.currentUser()).thenReturn(user);
        when(recipeRepository.findById(1)).thenReturn(Optional.of(recipe));

        // WHEN THEN
        assertThrows(ValidationException.class, () -> service.rateRecipe(1, 5));

    }

}
