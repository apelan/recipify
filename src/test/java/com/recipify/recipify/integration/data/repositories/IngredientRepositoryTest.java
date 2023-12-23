package com.recipify.recipify.integration.data.repositories;

import com.recipify.recipify.data.entities.Ingredient;
import com.recipify.recipify.data.repositories.IngredientRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("classpath:application.yml")
public class IngredientRepositoryTest {

    @Autowired
    private IngredientRepository repository;

    @Test
    public void findByNameIn_ingredientsFound() {
        List<Ingredient> result = repository.findByNameIn(Set.of("tomato", "carrot"));

        assertFalse(result.isEmpty());
        assertEquals("tomato", result.get(0).getName());
        assertEquals("carrot", result.get(1).getName());
    }

    @Test
    public void findByNameIn_ingredientsNotFound() {
        List<Ingredient> result = repository.findByNameIn(Set.of("spinach"));

        assertTrue(result.isEmpty());
    }

}
