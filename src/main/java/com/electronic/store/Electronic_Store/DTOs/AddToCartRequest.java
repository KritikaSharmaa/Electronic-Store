package com.electronic.store.Electronic_Store.DTOs;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddToCartRequest {
    private Long userId;
    private Long productId;
    private int quantity;
}
