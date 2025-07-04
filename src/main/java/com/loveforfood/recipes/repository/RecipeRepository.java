package com.loveforfood.recipes.repository;

import com.loveforfood.recipes.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing Recipe entities.
 * Provides methods to search recipes based on various criteria.
 */
@Repository
public interface RecipeRepository extends JpaRepository<Recipe,Long> {
    /**
     * Searches for recipes based on the provided criteria.
     *
     * @param vegetarian  filter by vegetarian status (null to ignore)
     * @param servings    filter by number of servings (null to ignore)
     * @param include     ingredient that must be included (null to ignore)
     * @param exclude     ingredient that must be excluded (null to ignore)
     * @param instructions keyword to search in instructions (null to ignore)
     * @return a list of recipes matching the search criteria
     */
    @Query("""
        SELECT DISTINCT r FROM Recipe r
        JOIN r.ingredients i
        WHERE (:vegetarian IS NULL OR r.vegetarian = :vegetarian)
          AND (:servings IS NULL OR r.servings = :servings)
          AND (:include IS NULL OR LOWER(i.name) = LOWER(:include))
          AND (:exclude IS NULL OR r.id NOT IN (
                                   SELECT r2.id FROM Recipe r2
                                   JOIN r2.ingredients i2
                                   WHERE LOWER(i2.name) = LOWER(:exclude)
                             ))
          AND (:instructions IS NULL OR LOWER(r.instructions) LIKE LOWER(CONCAT('%', :instructions, '%')))
    """)
    List<Recipe> search(Boolean vegetarian, Integer servings, String include, String exclude, String instructions);

    /**
     * Checks if a recipe with the given name exists, ignoring case.
     *
     * @param name the name of the recipe to check
     * @return true if a recipe with the given name exists, false otherwise
     */
    boolean existsByNameIgnoreCase(String name);
}
