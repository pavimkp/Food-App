package com.example.foodordering.controller;

import com.example.foodordering.dto.FoodItemRequest;
import com.example.foodordering.dto.FoodItemResponse;
import com.example.foodordering.dto.OrderResponse;
import com.example.foodordering.dto.RestaurantRequest;
import com.example.foodordering.dto.RestaurantResponse;
import com.example.foodordering.service.FoodItemService;
import com.example.foodordering.service.OrderService;
import com.example.foodordering.service.RestaurantService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Everything under /api/admin/** is protected by SecurityConfig (ROLE_ADMIN).
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final RestaurantService restaurantService;
    private final FoodItemService foodItemService;
    private final OrderService orderService;

    public AdminController(RestaurantService restaurantService,
                           FoodItemService foodItemService,
                           OrderService orderService) {
        this.restaurantService = restaurantService;
        this.foodItemService = foodItemService;
        this.orderService = orderService;
    }

    @GetMapping("/restaurants")
    public List<RestaurantResponse> listRestaurants() {
        return restaurantService.listAll();   // includes inactive ones
    }

    @PostMapping("/restaurants")
    @ResponseStatus(HttpStatus.CREATED)
    public RestaurantResponse createRestaurant(@Valid @RequestBody RestaurantRequest request) {
        return restaurantService.create(request);
    }

    @PostMapping("/food-items")
    @ResponseStatus(HttpStatus.CREATED)
    public FoodItemResponse createFoodItem(@Valid @RequestBody FoodItemRequest request) {
        return foodItemService.create(request);
    }

    @PutMapping("/food-items/{id}")
    public FoodItemResponse updateFoodItem(@PathVariable Long id, @Valid @RequestBody FoodItemRequest request) {
        return foodItemService.update(id, request);
    }

    @DeleteMapping("/food-items/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFoodItem(@PathVariable Long id) {
        foodItemService.delete(id);
    }

    @GetMapping("/orders")
    public List<OrderResponse> listOrders() {
        return orderService.listAll();
    }
}
