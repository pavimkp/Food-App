package com.example.foodordering.service;

import com.example.foodordering.dto.OrderItemRequest;
import com.example.foodordering.dto.OrderItemResponse;
import com.example.foodordering.dto.OrderRequest;
import com.example.foodordering.dto.OrderResponse;
import com.example.foodordering.entity.FoodItem;
import com.example.foodordering.entity.Order;
import com.example.foodordering.entity.OrderItem;
import com.example.foodordering.entity.OrderStatus;
import com.example.foodordering.exception.BadRequestException;
import com.example.foodordering.exception.ResourceNotFoundException;
import com.example.foodordering.repository.FoodItemRepository;
import com.example.foodordering.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    private static final int MAX_QUANTITY_PER_ITEM = 50;

    private final OrderRepository orderRepository;
    private final FoodItemRepository foodItemRepository;

    public OrderService(OrderRepository orderRepository, FoodItemRepository foodItemRepository) {
        this.orderRepository = orderRepository;
        this.foodItemRepository = foodItemRepository;
    }

    /**
     * One transaction: either the order AND all its lines are saved, or nothing is.
     * Prices and the total are calculated here from the database - never from the client.
     */
    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        // If the same food item appears twice, add the quantities together.
        Map<Long, Integer> quantityByFoodId = new LinkedHashMap<>();
        for (OrderItemRequest line : request.items()) {
            quantityByFoodId.merge(line.foodItemId(), line.quantity(), Integer::sum);
        }

        Order order = new Order();
        order.setCustomerName(request.customerName().trim());
        order.setPhone(request.phone().trim());
        order.setDeliveryAddress(request.deliveryAddress().trim());
        order.setPaymentMethod(request.paymentMethod() == null ? "COD" : request.paymentMethod());
        order.setStatus(OrderStatus.PLACED);

        BigDecimal total = BigDecimal.ZERO;

        for (Map.Entry<Long, Integer> entry : quantityByFoodId.entrySet()) {
            Long foodItemId = entry.getKey();
            int quantity = entry.getValue();

            FoodItem food = foodItemRepository.findById(foodItemId)
                    .orElseThrow(() -> new ResourceNotFoundException("Food item " + foodItemId + " not found"));

            if (!food.isAvailable() || !food.getRestaurant().isActive()) {
                throw new BadRequestException("'" + food.getName() + "' is currently unavailable");
            }
            if (quantity > MAX_QUANTITY_PER_ITEM) {
                throw new BadRequestException(
                        "Maximum " + MAX_QUANTITY_PER_ITEM + " of '" + food.getName() + "' per order");
            }

            BigDecimal unitPrice = food.getPrice();   // CURRENT price from the database
            BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity)).setScale(2, RoundingMode.HALF_UP);

            OrderItem line = new OrderItem();
            line.setFoodItem(food);
            line.setQuantity(quantity);
            line.setUnitPrice(unitPrice);             // snapshot kept for history
            line.setSubtotal(subtotal);
            order.addItem(line);

            total = total.add(subtotal);
        }

        order.setTotalAmount(total.setScale(2, RoundingMode.HALF_UP));
        return toResponse(orderRepository.save(order));
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrder(Long id) {
        Order order = orderRepository.findWithItemsById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order " + id + " not found"));
        return toResponse(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> listAll() {
        return orderRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(OrderService::toResponse).toList();
    }

    private static OrderResponse toResponse(Order o) {
        List<OrderItemResponse> items = o.getItems().stream()
                .map(i -> new OrderItemResponse(
                        i.getFoodItem().getId(),
                        i.getFoodItem().getName(),
                        i.getQuantity(),
                        i.getUnitPrice(),
                        i.getSubtotal()))
                .toList();
        return new OrderResponse(o.getId(), o.getCustomerName(), o.getPhone(), o.getDeliveryAddress(),
                o.getPaymentMethod(), o.getStatus().name(), o.getTotalAmount(), o.getCreatedAt(), items);
    }
}
