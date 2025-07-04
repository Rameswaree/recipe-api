package com.loveforfood.recipes.repository;

import com.loveforfood.recipes.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe,Long> {
    @Query("""
        SELECT DISTINCT r FROM Recipe r
        JOIN r.ingredients i
        WHERE (:vegetarian IS NULL OR r.vegetarian = :vegetarian)
          AND (:servings IS NULL OR r.servings = :servings)
          AND (:include IS NULL OR LOWER(i.name) = LOWER(:include))
          AND (:exclude IS NULL OR LOWER(i.name) != LOWER(:exclude))
          AND (:instructions IS NULL OR LOWER(r.instructions) LIKE LOWER(CONCAT('%', :instructions, '%')))
    """)
    List<Recipe> search(Boolean vegetarian, Integer servings, String include, String exclude, String instructions);
}
