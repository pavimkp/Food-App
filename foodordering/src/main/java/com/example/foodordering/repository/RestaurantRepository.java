package com.example.foodordering.repository;

import com.example.foodordering.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    // SELECT * FROM restaurants WHERE active = true
    List<Restaurant> findByActiveTrue();
}
