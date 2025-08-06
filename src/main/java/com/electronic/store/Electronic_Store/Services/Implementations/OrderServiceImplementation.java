package com.electronic.store.Electronic_Store.Services.Implementations;

import com.electronic.store.Electronic_Store.DTOs.OrderResponseDTO;
import com.electronic.store.Electronic_Store.Entities.Cart;
import com.electronic.store.Electronic_Store.Entities.Order;
import com.electronic.store.Electronic_Store.Entities.OrderItem;
import com.electronic.store.Electronic_Store.ExceptionHandler.CustomExceptions.ResourceNotFoundException;
import com.electronic.store.Electronic_Store.Repositories.CartRepository;
import com.electronic.store.Electronic_Store.Repositories.OrderRepository;
import com.electronic.store.Electronic_Store.Services.OrderService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImplementation implements OrderService {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    OrderRepository orderRepository;

    @Override
    @Transactional
    public OrderResponseDTO placeOrder(Long userId) {
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(()->new ResourceNotFoundException("Cart Not Found"));

        if (cart.getCartitems() == null || cart.getCartitems().isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }

        Order newOrder = new Order();
        newOrder.setUserId(userId);
        newOrder.setStatus("Pending");
        newOrder.setCreatedAt(LocalDateTime.now());

        List<OrderItem> orderItems = cart.getCartitems().stream().map(cartItem -> {
            OrderItem newOrderItem = new OrderItem();
            newOrderItem.setProduct_id(cartItem.getProduct_id());
            newOrderItem.setQuantity(cartItem.getQuantity());
            newOrderItem.setPrice(cartItem.getPrice());
            newOrderItem.setOrder(newOrder);
            return newOrderItem;
        }).collect(Collectors.toList());

        newOrder.setOrderItems(orderItems);

        Double total_amt= cart.getCartitems().stream().mapToDouble(item-> item.getPrice() * item.getQuantity()).sum();
        newOrder.setTotalAmount(total_amt);

        Order savedorder = orderRepository.save(newOrder);
        //Clear Cart
        cart.getCartitems().clear();
        cartRepository.save(cart);
        return modelMapper.map(newOrder, OrderResponseDTO.class);
    }

    @Override
    public List<OrderResponseDTO> getOrders(Long userId) {
        List<Order> orders = orderRepository.findAllByUserId(userId);
        if (orders.isEmpty()) {
            return Collections.emptyList();
        }
        return orders.stream()
                .map((order)-> modelMapper.map(order, OrderResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponseDTO getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(()->new ResourceNotFoundException("Order Not Found"));
        return modelMapper.map(order, OrderResponseDTO.class);
    }

    @Override
    public OrderResponseDTO updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId).orElseThrow(()->new ResourceNotFoundException("Order Not Found"));
        order.setStatus(status);
        orderRepository.save(order);
        return modelMapper.map(order, OrderResponseDTO.class);
    }

}
