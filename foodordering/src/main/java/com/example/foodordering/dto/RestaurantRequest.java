package com.example.foodordering.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RestaurantRequest(
        @NotBlank(message = "name is required")
        @Size(max = 100, message = "name must be at most 100 characters")
        String name,

        @Size(max = 500, message = "description must be at most 500 characters")
        String description,

        @NotBlank(message = "address is required")
        @Size(max = 255, message = "address must be at most 255 characters")
        String address,

        @Size(max = 500, message = "imageUrl must be at most 500 characters")
        String imageUrl,

        Boolean active
) {
}
