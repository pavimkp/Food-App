package com.example.foodordering.controller;

import com.example.foodordering.dto.FoodItemResponse;
import com.example.foodordering.service.FoodItemService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FoodItemController {

    private final FoodItemService foodItemService;

    public FoodItemController(FoodItemService foodItemService) {
        this.foodItemService = foodItemService;
    }

    // GET /api/restaurants/1/food-items
    @GetMapping("/api/restaurants/{restaurantId}/food-items")
    public List<FoodItemResponse> listByRestaurant(@PathVariable Long restaurantId) {
        return foodItemService.listByRestaurant(restaurantId);
    }

    // GET /api/food-items/search?name=pizza
    @GetMapping("/api/food-items/search")
    public List<FoodItemResponse> search(@RequestParam(name = "name", defaultValue = "") String name) {
        return foodItemService.search(name);
    }
}
