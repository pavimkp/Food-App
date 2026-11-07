package com.example.foodordering.controller;

import com.example.foodordering.dto.RestaurantResponse;
import com.example.foodordering.service.RestaurantService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping
    public List<RestaurantResponse> list() {
        return restaurantService.listActive();
    }

    @GetMapping("/{id}")
    public RestaurantResponse get(@PathVariable Long id) {
        return restaurantService.getActive(id);
    }
}
