package com.example.foodordering.dto;

import java.math.BigDecimal;

public record FoodItemResponse(
        Long id,
        Long restaurantId,
        String restaurantName,
        String name,
        String description,
        BigDecimal price,
        String imageUrl,
        boolean available
) {
}
