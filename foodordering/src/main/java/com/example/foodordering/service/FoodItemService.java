package com.example.foodordering.service;

import com.example.foodordering.dto.FoodItemRequest;
import com.example.foodordering.dto.FoodItemResponse;
import com.example.foodordering.entity.FoodItem;
import com.example.foodordering.entity.Restaurant;
import com.example.foodordering.exception.BadRequestException;
import com.example.foodordering.exception.ConflictException;
import com.example.foodordering.exception.ResourceNotFoundException;
import com.example.foodordering.repository.FoodItemRepository;
import com.example.foodordering.repository.OrderItemRepository;
import com.example.foodordering.repository.RestaurantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class FoodItemService {

    private final FoodItemRepository foodItemRepository;
    private final RestaurantRepository restaurantRepository;
    private final OrderItemRepository orderItemRepository;
    private final RestaurantService restaurantService;

    public FoodItemService(FoodItemRepository foodItemRepository,
                           RestaurantRepository restaurantRepository,
                           OrderItemRepository orderItemRepository,
                           RestaurantService restaurantService) {
        this.foodItemRepository = foodItemRepository;
        this.restaurantRepository = restaurantRepository;
        this.orderItemRepository = orderItemRepository;
        this.restaurantService = restaurantService;
    }

    @Transactional(readOnly = true)
    public List<FoodItemResponse> listByRestaurant(Long restaurantId) {
        restaurantService.findActiveEntity(restaurantId); // 404 if missing or inactive
        return foodItemRepository.findByRestaurantId(restaurantId).stream()
                .map(FoodItemService::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<FoodItemResponse> search(String name) {
        String text = name == null ? "" : name.trim();
        if (text.isEmpty()) {
            throw new BadRequestException("Search text 'name' must not be empty");
        }
        return foodItemRepository.findByNameContainingIgnoreCaseAndRestaurantActiveTrue(text).stream()
                .map(FoodItemService::toResponse).toList();
    }

    @Transactional
    public FoodItemResponse create(FoodItemRequest request) {
        Restaurant restaurant = restaurantRepository.findById(request.restaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant " + request.restaurantId() + " not found"));
        FoodItem item = new FoodItem();
        apply(item, request, restaurant);
        return toResponse(foodItemRepository.save(item));
    }

    @Transactional
    public FoodItemResponse update(Long id, FoodItemRequest request) {
        FoodItem item = foodItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Food item " + id + " not found"));
        Restaurant restaurant = restaurantRepository.findById(request.restaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant " + request.restaurantId() + " not found"));
        apply(item, request, restaurant);
        return toResponse(foodItemRepository.save(item));
    }

    @Transactional
    public void delete(Long id) {
        if (!foodItemRepository.existsById(id)) {
            throw new ResourceNotFoundException("Food item " + id + " not found");
        }
        // Past orders keep a reference to this item, so deleting it would break order history.
        if (orderItemRepository.existsByFoodItemId(id)) {
            throw new ConflictException(
                    "This item appears in past orders and cannot be deleted. Mark it unavailable instead.");
        }
        foodItemRepository.deleteById(id);
    }

    private void apply(FoodItem item, FoodItemRequest request, Restaurant restaurant) {
        item.setRestaurant(restaurant);
        item.setName(request.name().trim());
        item.setDescription(request.description());
        item.setPrice(request.price().setScale(2, RoundingMode.HALF_UP));
        item.setImageUrl(request.imageUrl());
        item.setAvailable(request.available() == null || request.available());
    }

    static FoodItemResponse toResponse(FoodItem f) {
        BigDecimal price = f.getPrice();
        return new FoodItemResponse(f.getId(), f.getRestaurant().getId(), f.getRestaurant().getName(),
                f.getName(), f.getDescription(), price, f.getImageUrl(), f.isAvailable());
    }
}
