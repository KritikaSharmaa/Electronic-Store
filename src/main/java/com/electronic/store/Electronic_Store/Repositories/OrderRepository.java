package com.electronic.store.Electronic_Store.Repositories;

import com.electronic.store.Electronic_Store.Entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    public
    List<Order> findAllByUserId(Long userId);
}
