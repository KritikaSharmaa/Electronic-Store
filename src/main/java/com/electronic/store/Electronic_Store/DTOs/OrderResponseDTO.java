package com.electronic.store.Electronic_Store.DTOs;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDTO {
    private Long orderId;
    private Double totalAmount;
    private String status;
    private List<OrderItemDTO> orderItems;
    private LocalDateTime createdAt;
}
