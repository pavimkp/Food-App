package com.example.foodordering.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

// Note: NO price here. The server always uses the price stored in the database.
public record OrderItemRequest(
        @NotNull(message = "foodItemId is required")
        Long foodItemId,

        @NotNull(message = "quantity is required")
        @Min(value = 1, message = "quantity must be at least 1")
        @Max(value = 50, message = "quantity must be at most 50")
        Integer quantity
) {
}
