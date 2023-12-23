package com.recipify.recipify.integration.data.repositories;

import com.recipify.recipify.data.entities.RecipeRating;
import com.recipify.recipify.data.repositories.RecipeRatingRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("classpath:application.yml")
public class RecipeRatingRepositoryTest {

    @Autowired
    private RecipeRatingRepository repository;

    @Test
    public void findByRecipeIdAndUserId_ratingFound() {
        Optional<RecipeRating> result = repository.findByRecipeIdAndUserId(1, 1);

        assertTrue(result.isPresent());
        assertEquals(5, result.get().getRating());
    }

    @Test
    public void findByRecipeIdAndUserId_ratingNotFound() {
        Optional<RecipeRating> result = repository.findByRecipeIdAndUserId(2, 1);

        assertFalse(result.isPresent());
    }

}
