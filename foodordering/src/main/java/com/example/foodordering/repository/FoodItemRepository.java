package com.example.foodordering.repository;

import com.example.foodordering.entity.FoodItem;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {

    // EntityGraph loads the restaurant in the same query (avoids many small queries).
    @EntityGraph(attributePaths = "restaurant")
    List<FoodItem> findByRestaurantId(Long restaurantId);

    // Case-insensitive "contains" search, only for active restaurants.
    @EntityGraph(attributePaths = "restaurant")
    List<FoodItem> findByNameContainingIgnoreCaseAndRestaurantActiveTrue(String name);
}
