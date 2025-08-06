package com.electronic.store.Electronic_Store.DTOs;

import com.electronic.store.Electronic_Store.Entities.CartItem;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartResponseDTO {
    private int total_items;
    private double total_amount;
    private List<CartItem> cartItemList;
}
