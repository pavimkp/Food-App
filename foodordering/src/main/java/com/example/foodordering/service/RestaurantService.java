package com.example.foodordering.service;

import com.example.foodordering.dto.RestaurantRequest;
import com.example.foodordering.dto.RestaurantResponse;
import com.example.foodordering.entity.Restaurant;
import com.example.foodordering.exception.ResourceNotFoundException;
import com.example.foodordering.repository.RestaurantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    // Constructor injection: Spring supplies the repository automatically.
    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Transactional(readOnly = true)
    public List<RestaurantResponse> listActive() {
        return restaurantRepository.findByActiveTrue().stream().map(RestaurantService::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<RestaurantResponse> listAll() {
        return restaurantRepository.findAll().stream().map(RestaurantService::toResponse).toList();
    }

    // Public lookup: inactive restaurants look like they do not exist.
    @Transactional(readOnly = true)
    public RestaurantResponse getActive(Long id) {
        return toResponse(findActiveEntity(id));
    }

    @Transactional(readOnly = true)
    public Restaurant findActiveEntity(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant " + id + " not found"));
        if (!restaurant.isActive()) {
            throw new ResourceNotFoundException("Restaurant " + id + " not found");
        }
        return restaurant;
    }

    @Transactional
    public RestaurantResponse create(RestaurantRequest request) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(request.name().trim());
        restaurant.setDescription(request.description());
        restaurant.setAddress(request.address().trim());
        restaurant.setImageUrl(request.imageUrl());
        restaurant.setActive(request.active() == null || request.active());
        return toResponse(restaurantRepository.save(restaurant));
    }

    static RestaurantResponse toResponse(Restaurant r) {
        return new RestaurantResponse(r.getId(), r.getName(), r.getDescription(),
                r.getAddress(), r.getImageUrl(), r.isActive());
    }
}
