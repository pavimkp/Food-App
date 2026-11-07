package com.example.foodordering.dto;

public record RestaurantResponse(
        Long id,
        String name,
        String description,
        String address,
        String imageUrl,
        boolean active
) {
}
