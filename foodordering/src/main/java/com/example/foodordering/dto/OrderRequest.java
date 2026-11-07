package com.example.foodordering.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

// Note: NO totalAmount here. The server calculates it.
public record OrderRequest(
        @NotBlank(message = "customerName is required")
        @Size(max = 100, message = "customerName must be at most 100 characters")
        String customerName,

        @NotBlank(message = "phone is required")
        @Pattern(regexp = "^[0-9+\\- ]{7,20}$", message = "phone must be 7-20 characters: digits, +, - or spaces")
        String phone,

        @NotBlank(message = "deliveryAddress is required")
        @Size(max = 255, message = "deliveryAddress must be at most 255 characters")
        String deliveryAddress,

        // Optional. COD (cash on delivery) is the default; SIMULATED = fake payment.
        @Pattern(regexp = "^(COD|SIMULATED)$", message = "paymentMethod must be COD or SIMULATED")
        String paymentMethod,

        @NotEmpty(message = "items must contain at least one item")
        @Valid
        List<OrderItemRequest> items
) {
}
