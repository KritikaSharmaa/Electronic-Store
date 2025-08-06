package com.electronic.store.Electronic_Store.Entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    private String productName;
    private String description;
    private String shortDescription;
    private Double price;
    private int quantity;
    private boolean inStock;
    private Double avgRatings;
    private int numberOfRatings;
    private String productImage;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
