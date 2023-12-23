package com.recipify.recipify.unit.services;

import com.recipify.recipify.api.dto.CreateRecipeDto;
import com.recipify.recipify.api.dto.RecipeDto;
import com.recipify.recipify.api.enumerator.IngredientsCountSortType;
import com.recipify.recipify.data.entities.Ingredient;
import com.recipify.recipify.data.entities.Recipe;
import com.recipify.recipify.data.entities.RecipeIngredient;
import com.recipify.recipify.data.entities.User;
import com.recipify.recipify.data.repositories.RecipeIngredientsRepository;
import com.recipify.recipify.data.repositories.RecipeRepository;
import com.recipify.recipify.services.IngredientService;
import com.recipify.recipify.services.RecipeService;
import com.recipify.recipify.services.UserDetailsService;
import com.recipify.recipify.unit.CommonMockObjects;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private IngredientService ingredientService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private RecipeIngredientsRepository recipeIngredientsRepository;

    @InjectMocks
    private RecipeService service;

    @Captor
    private ArgumentCaptor<Recipe> recipeArgumentCaptor;
    @Captor
    private ArgumentCaptor<List<RecipeIngredient>> listRecipeIngredientArgumentCaptor;

    @Test
    public void createRecipe() {
        // GIVEN
        User user = CommonMockObjects.getMockUser();

        CreateRecipeDto dto = new CreateRecipeDto("test", "test",
                Set.of("test-ingredient1", "test-ingredient2"));

        List<Ingredient> ingredients = List.of(
                CommonMockObjects.getMockIngredient("test-ingredient1"),
                CommonMockObjects.getMockIngredient("test-ingredient2")
        );

        when(userDetailsService.currentUser()).thenReturn(user);
        when(ingredientService.checkExistingIngredients(Set.of("test-ingredient1", "test-ingredient2")))
                .thenReturn(ingredients);

        // WHEN
        service.createRecipe(dto);

        // THEN
        verify(recipeRepository).save(recipeArgumentCaptor.capture());
        verify(recipeIngredientsRepository).saveAll(listRecipeIngredientArgumentCaptor.capture());

    }

    @Test
    public void listRecipes_defaultValues() {
        // GIVEN
        Page<RecipeDto> page = CommonMockObjects.getMockPageRecipeDto();
        List<RecipeDto> list = List.of(CommonMockObjects.getMockRecipeDto());

        Pageable pageable = PageRequest.of(0, 10, Sort.by("ingredientCount").ascending());
        when(recipeRepository.findAllRecipes("", pageable)).thenReturn(page);
        when(ingredientService.attachIngredientToRecipes(page.stream().toList())).thenReturn(list);

        // WHEN
        List<RecipeDto> result = service.listRecipes(0, 10, "", IngredientsCountSortType.LEAST);

        // THEN
        assertEquals(list.size(), result.size());
        assertEquals("test name", result.get(0).name());
        assertEquals("test description", result.get(0).description());
        assertEquals("test owner", result.get(0).ownerName());
        assertEquals(5, result.get(0).rating());

    }

    @Test
    public void listRecipes_nonDefaultInputValues() {
        // GIVEN
        Page<RecipeDto> page = CommonMockObjects.getMockPageRecipeDto();
        List<RecipeDto> list = List.of(CommonMockObjects.getMockRecipeDto());

        Pageable pageable = PageRequest.of(5, 20, Sort.by("ingredientCount").descending());
        when(recipeRepository.findAllRecipes("test", pageable)).thenReturn(page);
        when(ingredientService.attachIngredientToRecipes(page.stream().toList())).thenReturn(list);

        // WHEN
        List<RecipeDto> result = service
                .listRecipes(5, 20, "test", IngredientsCountSortType.MOST);

        // THEN
        assertEquals(list.size(), result.size());
        assertEquals("test name", result.get(0).name());
        assertEquals("test description", result.get(0).description());
        assertEquals("test owner", result.get(0).ownerName());
        assertEquals(5, result.get(0).rating());

    }

    @Test
    public void listOwnedRecipes() {
        // GIVEN
        Page<RecipeDto> page = CommonMockObjects.getMockPageRecipeDto();
        List<RecipeDto> list = List.of(CommonMockObjects.getMockRecipeDto());
        User user = CommonMockObjects.getMockUser();

        when(userDetailsService.currentUser()).thenReturn(user);
        when(recipeRepository.findAllOwnedRecipes(user.getId(), PageRequest.of(0, 10)))
                .thenReturn(page);
        when(ingredientService.attachIngredientToRecipes(page.stream().toList())).thenReturn(list);

        // WHEN
        List<RecipeDto> result = service.listOwnedRecipes(0, 10);

        // THEN
        assertEquals(list.size(), result.size());
        assertEquals("test name", result.get(0).name());
        assertEquals("test description", result.get(0).description());
        assertEquals("test owner", result.get(0).ownerName());
        assertEquals(5, result.get(0).rating());

    }


}
