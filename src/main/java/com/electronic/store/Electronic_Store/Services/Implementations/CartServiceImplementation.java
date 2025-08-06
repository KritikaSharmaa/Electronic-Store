package com.electronic.store.Electronic_Store.Services.Implementations;

import com.electronic.store.Electronic_Store.DTOs.CartResponseDTO;
import com.electronic.store.Electronic_Store.Entities.Cart;
import com.electronic.store.Electronic_Store.Entities.CartItem;
import com.electronic.store.Electronic_Store.Entities.Product;
import com.electronic.store.Electronic_Store.Repositories.CartItemRepository;
import com.electronic.store.Electronic_Store.Repositories.CartRepository;
import com.electronic.store.Electronic_Store.Repositories.ProductRepository;
import com.electronic.store.Electronic_Store.Services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class CartServiceImplementation implements CartService {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    ProductRepository productRepository;

    @Override
    public CartResponseDTO getCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(()-> new RuntimeException("Cart Not Found"));
        CartResponseDTO responseDTO = new CartResponseDTO();
        responseDTO.setCartItemList(cart.getCartitems());

        int totalItems = cart.getCartitems().stream().mapToInt(CartItem::getQuantity).sum();
        double totalAmount = cart.getCartitems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        responseDTO.setTotal_items(totalItems);
        responseDTO.setTotal_amount(totalAmount);
        return responseDTO;
    }

    @Override
    public String addItemToCart(Long userId, Long productId, int quantity) {
        Product product = productRepository.findById(productId).orElseThrow(()-> new RuntimeException("Product Not Found"));
        Cart cart = cartRepository.findByUserId(userId).orElseGet(()->{
            Cart c = new Cart();
            c.setUserId(userId);
            c.setCartitems(new ArrayList<>());
            return cartRepository.save(c);
        });
        System.out.println("cart.................."+cart);
        Optional<CartItem> existingItem = cart.getCartitems().stream().filter(item->item.getProduct_id().equals(productId)).findFirst();
        System.out.println(existingItem);
        if(existingItem.isPresent()){
            System.out.println("product already in the cart..");
            existingItem.get().setQuantity(existingItem.get().getQuantity()+quantity);
        }else{
            CartItem new_item = new CartItem();
            new_item.setProduct_id(productId);
            new_item.setQuantity(quantity);
            new_item.setPrice(product.getPrice());
            new_item.setCart(cart);
            cart.getCartitems().add(new_item);
        }

        cartRepository.save(cart);
        return "Successfully added "+quantity+"item/s of "+product.getProductName()+" into your cart..";
    }

    @Override
    public String removeItemFromCart(Long userId, Long productId) {
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(()-> new RuntimeException("Cart not found"));
        cart.getCartitems().removeIf(item -> item.getProduct_id().equals(productId));
        cartRepository.save(cart);
        return "Removed item: "+ productId+" successfully from cart..";
    }

    @Override
    public String updateItemQuantity(Long userId, Long productId, int quantity) {
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(()-> new RuntimeException("Cart not found"));
        Optional<CartItem> existingItem = cart.getCartitems().stream().filter(item->item.getProduct_id().equals(productId)).findFirst();
        if(existingItem.get().getQuantity()+quantity>=1){
            existingItem.get().setQuantity(existingItem.get().getQuantity()+quantity);
        }else{
            cart.getCartitems().removeIf(item -> item.getProduct_id().equals(productId));
        }
        cartRepository.save(cart);
        return  "updated successfully";
    }

    @Override
    public String clearCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(()-> new RuntimeException("Cart not found"));
        cart.getCartitems().clear();
        cartRepository.save(cart);
        return "Cleared cart successfully";
    }
}
