package com.example.foodordering.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record FoodItemRequest(
        @NotNull(message = "restaurantId is required")
        Long restaurantId,

        @NotBlank(message = "name is required")
        @Size(max = 100, message = "name must be at most 100 characters")
        String name,

        @Size(max = 500, message = "description must be at most 500 characters")
        String description,

        @NotNull(message = "price is required")
        @DecimalMin(value = "0.00", message = "price must not be negative")
        @Digits(integer = 8, fraction = 2, message = "price must have at most 2 decimal places")
        BigDecimal price,

        @Size(max = 500, message = "imageUrl must be at most 500 characters")
        String imageUrl,

        Boolean available
) {
}
