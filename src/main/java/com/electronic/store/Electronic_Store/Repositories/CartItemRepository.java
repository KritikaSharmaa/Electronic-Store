package com.electronic.store.Electronic_Store.Repositories;

import com.electronic.store.Electronic_Store.Entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
