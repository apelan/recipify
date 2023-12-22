package com.recipify.recipify.data.repositories;

import com.recipify.recipify.data.entities.Ingredient;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {
    List<Ingredient> findByNameIn(Set<String> names);
}
