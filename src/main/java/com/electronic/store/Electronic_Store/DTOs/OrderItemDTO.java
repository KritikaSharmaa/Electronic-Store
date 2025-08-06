package com.electronic.store.Electronic_Store.DTOs;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {
        private Long product_id;
        private int quantity;
        private double price;
}
