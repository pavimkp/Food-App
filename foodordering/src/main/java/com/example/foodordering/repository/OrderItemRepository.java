package com.example.foodordering.repository;

import com.example.foodordering.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // Used before deleting a food item: has anyone ever ordered it?
    boolean existsByFoodItemId(Long foodItemId);
}
