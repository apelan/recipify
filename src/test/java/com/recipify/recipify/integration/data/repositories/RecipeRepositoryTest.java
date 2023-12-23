package com.recipify.recipify.integration.data.repositories;

import com.recipify.recipify.api.dto.RecipeDto;
import com.recipify.recipify.data.repositories.RecipeRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("classpath:application.yml")
public class RecipeRepositoryTest {

    @Autowired
    private RecipeRepository repository;

    @Test
    public void findAllRecipes_allResults() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("ingredientCount").ascending());
        Page<RecipeDto> result = repository.findAllRecipes("", pageable);

        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalPages());
        assertEquals(10, result.getSize());
        assertEquals(2, result.getTotalElements()); // only two in test db
    }

    @Test
    public void findAllRecipes_paginatedResult() {
        Pageable pageable = PageRequest.of(1, 1, Sort.by("ingredientCount").ascending());
        Page<RecipeDto> result = repository.findAllRecipes("", pageable);

        assertFalse(result.isEmpty());
        assertEquals(2, result.getTotalPages());
        assertEquals(1, result.getSize());
        assertEquals(2, result.getTotalElements()); // only two in test db
    }

    @Test
    public void findAllRecipes_sortIngredientsCount_ascending() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("ingredientCount").ascending());
        Page<RecipeDto> result = repository.findAllRecipes("", pageable);

        assertFalse(result.isEmpty());

        Iterator<RecipeDto> iterator = result.iterator();
        RecipeDto firstElement = iterator.next();
        RecipeDto secondElement = iterator.next();
        assertEquals("british breakfast", firstElement.name());
        assertEquals("chicken soup", secondElement.name());
    }

    @Test
    public void findAllRecipes_sortIngredientsCount_descending() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("ingredientCount").descending());
        Page<RecipeDto> result = repository.findAllRecipes("", pageable);

        assertFalse(result.isEmpty());

        Iterator<RecipeDto> iterator = result.iterator();
        RecipeDto firstElement = iterator.next();
        RecipeDto secondElement = iterator.next();
        assertEquals("chicken soup", firstElement.name());
        assertEquals("british breakfast", secondElement.name());
    }

    @Test
    public void findAllRecipes_search_byName() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("ingredientCount").ascending());
        Page<RecipeDto> result = repository.findAllRecipes("chicken soup", pageable);

        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
        assertEquals("chicken soup", result.iterator().next().name());
    }

    @Test
    public void findAllRecipes_search_byDescriptionFuzzy() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("ingredientCount").ascending());
        Page<RecipeDto> result = repository.findAllRecipes("orem", pageable);

        assertFalse(result.isEmpty());
        Iterator<RecipeDto> iterator = result.iterator();
        RecipeDto firstElement = iterator.next();
        RecipeDto secondElement = iterator.next();
        assertEquals("british breakfast", firstElement.name());
        assertEquals("chicken soup", secondElement.name());
    }

    @Test
    public void findAllRecipes_search_byIngredientName_multiple() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("ingredientCount").ascending());
        Page<RecipeDto> result = repository.findAllRecipes("salt", pageable);

        assertFalse(result.isEmpty());
        Iterator<RecipeDto> iterator = result.iterator();
        RecipeDto firstElement = iterator.next();
        RecipeDto secondElement = iterator.next();
        assertEquals("british breakfast", firstElement.name());
        assertEquals("chicken soup", secondElement.name());
    }

    @Test
    public void findAllRecipes_search_byIngredientName_single() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("ingredientCount").ascending());
        Page<RecipeDto> result = repository.findAllRecipes("chicken", pageable);

        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
        assertEquals("chicken soup", result.iterator().next().name());
    }

    @Test
    public void findAllOwnedRecipes() {
        Pageable pageable = PageRequest.of(0, 10);

        // find recipes for user with id 1
        Page<RecipeDto> result1 = repository.findAllOwnedRecipes(1, pageable);
        assertFalse(result1.isEmpty());
        assertEquals(1, result1.getTotalElements());
        assertEquals("chicken soup", result1.iterator().next().name());

        // find recipes for user with id 2
        Page<RecipeDto> result2 = repository.findAllOwnedRecipes(2, pageable);
        assertFalse(result2.isEmpty());
        assertEquals(1, result2.getTotalElements());
        assertEquals("british breakfast", result2.iterator().next().name());

    }

}
