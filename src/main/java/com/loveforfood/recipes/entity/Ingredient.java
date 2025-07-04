package com.loveforfood.recipes.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Represents an ingredient in a recipe.
 * Each ingredient is associated with a specific recipe.
 */
@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;
}
