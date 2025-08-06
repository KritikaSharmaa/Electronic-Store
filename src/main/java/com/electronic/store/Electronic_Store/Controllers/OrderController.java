package com.electronic.store.Electronic_Store.Controllers;

import com.electronic.store.Electronic_Store.DTOs.OrderResponseDTO;
import com.electronic.store.Electronic_Store.Services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping("/{uid}")
    @PreAuthorize("#uid == authentication.principal.userId")
    public ResponseEntity<OrderResponseDTO> placeOrder(@PathVariable Long uid){
        return new ResponseEntity<>(orderService.placeOrder(uid), HttpStatus.CREATED);
    }

    @GetMapping("/user/{uid}")
    @PreAuthorize("#uid == authentication.principal.userId")
    public ResponseEntity<List<OrderResponseDTO>> getOrders(@PathVariable Long uid){
        return new ResponseEntity<>(orderService.getOrders(uid), HttpStatus.OK);
    }

    @GetMapping("/{oid}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long oid){
        return new ResponseEntity<>(orderService.getOrderById(oid), HttpStatus.OK);
    }

    @PutMapping("/{oid}/status")
    public ResponseEntity<OrderResponseDTO> getOrderStatusUpdated(@PathVariable Long oid, @RequestBody Map<String, String> request){
        String newStatus = request.get("status");
        return new ResponseEntity<>(orderService.updateOrderStatus(oid, newStatus), HttpStatus.OK);
    }
}
