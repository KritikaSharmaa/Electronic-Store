package com.electronic.store.Electronic_Store.Services;

import com.electronic.store.Electronic_Store.DTOs.CartResponseDTO;
import org.springframework.http.ResponseEntity;

public interface CartService {
    //1. Get Cart by userId
    public CartResponseDTO getCart(Long userId);

    //2. Add To Cart
    public String addItemToCart(Long userId, Long productId, int quantity);

    //3. Remove Item from Cart
    public String removeItemFromCart(Long userId, Long productId);

    //4. Update Item Quantity
    public String updateItemQuantity(Long userId, Long product, int quantity);

    //5. Clear Cart (Optional)
    public String clearCart(Long userId);
}
