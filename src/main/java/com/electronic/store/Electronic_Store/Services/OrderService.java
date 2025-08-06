package com.electronic.store.Electronic_Store.Services;

import com.electronic.store.Electronic_Store.DTOs.OrderResponseDTO;

import java.util.List;

public interface OrderService {
    //1. Place Order
    public OrderResponseDTO placeOrder(Long userId);

    //2. Get All Orders for a User
    public List<OrderResponseDTO> getOrders(Long userId);

    //3. Get details of a single order
    public OrderResponseDTO getOrderById(Long orderId);

    //4. Update status of Order(Pending/Paid/Cancelled/Shipped/Delivered/Returned)
    public OrderResponseDTO updateOrderStatus(Long orderId, String status);
}
