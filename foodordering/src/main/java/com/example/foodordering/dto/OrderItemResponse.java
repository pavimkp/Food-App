package com.example.foodordering.dto;

import java.math.BigDecimal;

public record OrderItemResponse(
        Long foodItemId,
        String foodName,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal subtotal
) {
}
