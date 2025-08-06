package com.electronic.store.Electronic_Store.Controllers;

import com.electronic.store.Electronic_Store.DTOs.AddToCartRequest;
import com.electronic.store.Electronic_Store.DTOs.CartResponseDTO;
import com.electronic.store.Electronic_Store.Services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    CartService cartService;

    @GetMapping("/{uid}")
    @PreAuthorize("#uid == authentication.principal.userId")
    public ResponseEntity<CartResponseDTO> getCart(@PathVariable Long uid){
        return new ResponseEntity<>(cartService.getCart(uid), HttpStatus.OK);
    }

    @PostMapping("/add")
    @PreAuthorize("#request.userId == authentication.principal.userId")
    public ResponseEntity<String> addToCart(@RequestBody AddToCartRequest request){
        return new ResponseEntity<>(cartService.addItemToCart(request.getUserId(), request.getProductId(), request.getQuantity()), HttpStatus.CREATED);
    }

    @DeleteMapping("/{uid}")
    @PreAuthorize("#uid == authentication.principal.userId")
    public ResponseEntity<String> clearCart(@PathVariable Long uid){
        return new ResponseEntity<>(cartService.clearCart(uid),HttpStatus.OK);
    }

    @DeleteMapping("/{uid}/product/{pid}")
    @PreAuthorize("#uid == authentication.principal.userId")
    public ResponseEntity<String>  removeProductFromCart(@PathVariable Long uid, @PathVariable Long pid){
        return  new ResponseEntity<>(cartService.removeItemFromCart(uid, pid), HttpStatus.OK);
    }

    @PutMapping("/{uid}/items")
    @PreAuthorize("#uid == authentication.principal.userId")
    public ResponseEntity<String> updateProductQuantity(@PathVariable Long uid,@RequestBody AddToCartRequest request){
        return  new ResponseEntity<>(cartService.updateItemQuantity(uid, request.getProductId(), request.getQuantity()), HttpStatus.OK);
    }

}
