package com.electronic.store.Electronic_Store.Repositories;

import com.electronic.store.Electronic_Store.Entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
