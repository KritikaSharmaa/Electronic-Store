package com.electronic.store.Electronic_Store.Repositories;

import com.electronic.store.Electronic_Store.Entities.Category;
import com.electronic.store.Electronic_Store.Entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAllByInStock(Boolean inStock, Pageable pageable);

    Page<Product> findByPriceBetween(Double minPrice, Double maxPrice, Pageable pageable);

    Page<Product> findByProductNameContainingIgnoreCaseOrShortDescriptionContainingIgnoreCase(
            String nameKeyword,
            String descKeyword,
            Pageable pageable
    );

    Page<Product> findByCategory(Category category, Pageable pageable);
}
